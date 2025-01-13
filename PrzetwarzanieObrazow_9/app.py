import cv2
import numpy as np
import pytesseract
import threading




class VideoCaptureThread(threading.Thread):
    def __init__(self, rtsp_url):
        super().__init__()
        self.rtsp_url = rtsp_url
        self.cap = cv2.VideoCapture(rtsp_url)
        self.frame = None
        self.running = True

    def run(self):
        while self.running:
            ret, frame = self.cap.read()
            if ret:
                self.frame = frame

    def stop(self):
        self.running = False
        self.cap.release()

pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'
# Funkcja do wykrywania pożaru na podstawie temperatury
def detect_fire(frame, threshold_temp=60):
    max_temp = 150  # Zakładamy, że maksymalna temperatura to 150 stopni Celsjusza
    temp_frame = frame * (max_temp / 255)  # Przeskalowanie wartości piksela do temperatury
    fire_mask = temp_frame >= threshold_temp
    return fire_mask

# Funkcja do odczytu temperatury z paska
# Function to read temperature from the bar using OCR

def read_temperature_from_bar(frame):
    bar_area = frame[30:]  # Adjust the coordinates to your camera
    gray_bar = cv2.cvtColor(bar_area, cv2.COLOR_BGR2GRAY)
    text = pytesseract.image_to_string(gray_bar, config='psm 6')
    print(f"Read text from bar: {text}")

    temperatures = []
    for word in text.split():
        try:
            temperature = float(word.replace('°C', ''))
            temperatures.append(temperature)
        except ValueError:
            continue

    if temperatures:
        highest_temperature = max(temperatures)
        if highest_temperature > 100:
            print("WYKRYTO POŻAR UCIEKAĆ!!!!!!!!")
        elif highest_temperature > 60:
            print("Wykryto wysoką temperaturę: ", highest_temperature)
        return highest_temperature
    return None

rtsp_url = "rtsp://ZTC:ZTC@192.168.1.205/profile4"
video_thread = VideoCaptureThread(rtsp_url)
video_thread.start()

while True:
    frame = video_thread.frame
    if frame is not None:
        frame = cv2.resize(frame, (640, 480))
        gray_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        temperature = read_temperature_from_bar(frame)
        if temperature is not None and temperature > 60:
            fire_mask = detect_fire(gray_frame, threshold_temp=60)
            contours, _ = cv2.findContours(fire_mask.astype(np.uint8), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
            for contour in contours:
                if cv2.contourArea(contour) > 500:
                    x, y, w, h = cv2.boundingRect(contour)
                    cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
                    cv2.putText(frame, "Fire Detected", (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 2)
        cv2.imshow('Thermal Camera', frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

video_thread.stop()
cv2.destroyAllWindows()