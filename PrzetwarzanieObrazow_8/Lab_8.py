import cv2
from ultralytics import YOLO


# Funkcja do rysowania prostokątów wokół wykrytych samochodów
def draw_boxes(frame, detections, class_names):
    for detection in detections:
        # Koordynaty prostokąta
        x1, y1, x2, y2 = map(int, detection[:4])
        conf = detection[4]
        cls_id = int(detection[5])

        # Filtruj tylko klasy samochodów
        if class_names[cls_id] in ["car", "truck", "bus"]:
            label = f"{class_names[cls_id]} {conf:.2f}"

            # Rysowanie zielonego prostokąta
            cv2.rectangle(frame, (x1, y1), (x2, y2), (0, 255, 0), 2)
            cv2.putText(frame, label, (x1, y1 - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 255, 0), 2)

    return frame


# Wczytanie modelu YOLO
model = YOLO("yolov8n.pt")  # Wersja 'n' (nano) YOLOv8 dla szybkich rezultatów

# Ścieżka do wideo
video_path = r"C:\Users\doria\PycharmProjects\ImageProcessingProjects\27260-362770008_medium.mp4"  # Zmień na ścieżkę do swojego pliku wideo
output_path = "output_video.mp4"

# Wczytanie wideo
cap = cv2.VideoCapture(video_path)
fourcc = cv2.VideoWriter_fourcc(*'mp4v')  # Format wyjściowego wideo
out = None

# Klasy obiektów w modelu YOLO
class_names = model.names

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        break

    # Wykrywanie obiektów na aktualnej klatce
    results = model.predict(frame, conf=0.5, iou=0.4, show=False)
    detections = results[0].boxes.data.cpu().numpy()  # Koordynaty i klasy

    # Rysowanie wykrytych samochodów na klatce
    frame = draw_boxes(frame, detections, class_names)

    # Tworzenie obiektu do zapisu wideo (jeśli jeszcze nie istnieje)
    if out is None:
        h, w, _ = frame.shape
        out = cv2.VideoWriter(output_path, fourcc, 30, (w, h))

    # Zapis klatki
    out.write(frame)

    # Opcjonalnie wyświetl wideo
    cv2.imshow("Detekcja samochodów", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Zwalnianie zasobów
cap.release()
out.release()
cv2.destroyAllWindows()