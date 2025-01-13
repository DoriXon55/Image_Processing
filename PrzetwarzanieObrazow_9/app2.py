import cv2
import numpy as np
import pytesseract

# Funkcja do analizy temperatury z paska za pomocą OCR
def get_temperature_from_overlay(frame):
    # Wytnij obszar, w którym wyświetlana jest temperatura
    overlay_region = frame[50:100, 400:600]  # Dostosuj współrzędne do swojego obrazu

    # Przekształcenie do odcieni szarości i poprawa kontrastu
    gray = cv2.cvtColor(overlay_region, cv2.COLOR_BGR2GRAY)
    _, binary = cv2.threshold(gray, 150, 255, cv2.THRESH_BINARY)

    # Użycie pytesseract do odczytania tekstu
    config = "--psm 7"  # PSM 7 to konfiguracja dla pojedynczej linii tekstu
    text = pytesseract.image_to_string(binary, config=config)

    # Wyodrębnienie liczby z tekstu
    try:
        temperature = float(text.split()[0])  # Zakładamy, że liczba jest pierwsza w tekście
    except ValueError:
        temperature = None  # Nie udało się odczytać liczby

    return temperature

# Adres URL strumienia RTSP kamery
rtsp_url = "rtsp://ZTC:ZTC@192.168.1.205/profile4"  # Zmień na właściwy adres

# Wczytanie strumienia wideo z kamery
cap = cv2.VideoCapture(rtsp_url)

if not cap.isOpened():
    print("Błąd: Nie można otworzyć kamery.")
    exit()

while cap.isOpened():
    ret, frame = cap.read()
    if not ret:
        print("Błąd: Nie można odczytać klatki wideo.")
        break

    # Odczyt temperatury z paska
    temperature = get_temperature_from_overlay(frame)

    # Sprawdzenie, czy temperatura przekracza próg
    if temperature is not None and temperature > 90:
        print(f"Temperatura przekracza 90°C: {temperature}°C")
        cv2.putText(frame, f"High Temp: {temperature}°C", (50, 50),
                    cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 0, 255), 2)

    # Wyświetlanie klatki
    cv2.imshow('Thermal Camera', frame)

    if cv2.waitKey(30) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()