[<img src='images/en.png' height=10> English version](README.md)  
[<img src='images/de.png' height=10> Deutsche Version](README_DE.md)

# Glucose Data Handler

## Funkcje

* odbiera wartości glukozy jako **[LibreLink follower](#librelink)**
* odbiera wartości glukozy z Nightscout
* odbiera wartości glukozy z Juggluco
* odbiera wartości glukozy z xDrip+
* udostępnia kilka **[widgetów](#widgety)** i pływający widget dla telefonu
* udostępnia opcjonalne **[powiadomienia](#powiadomienia)** z różnymi ikonami dla telefonu
* udostępnia kilka **[komplikacj](#komplikacje)** dla Wear OS
* umożliwia połączenie z **[Android Auto](./GlucoDataAuto_PL.md)**
* Integracja z aplikacją **[Tasker](#aplikacja-tasker)**
* wysyła transmisje danych o glukozie do innych aplikacji (które obsługują tę transmisję)

## Pobierz

[<img src='https://play.google.com/intl/en_us/badges/static/images/badges/pl_badge_web_generic.png' height=100>](https://play.google.com/store/apps/details?id=de.michelinside.glucodatahandler) 

Aktualną wersję można pobrać [tutaj](https://github.com/pachi81/GlucoDataHandler/releases).

## Instalacja

-> [Instrukcja instalacji](./INSTALLATION_PL.md)

## Ustawienia

## Zegarek

* Wibracja: zegarek wibruje, jeśli zakres docelowy został przekroczony i powtarza wibracje tak długo, jak długo glukoza pozostaje poza zakresem docelowym
* Kolorowy AOD: niektóre tarcze zegarków obsługują tylko kolorowe komplikacje dla zawsze włączonego ekranu w trybie AOD, jeśli nie ma monochromatycznego, musisz aktywować tę funkcję
* Duża strzałka trendu: w przypadku zegarków takich jak Samsung Galaxy Watch 5 Pro do wersji Wear OS 3 strzałka trendu jest zbyt duża, więc można wyłączyć to ustawienie, aby uzyskać mniejszą strzałkę trendu
* Pierwszy plan: opcja zalecana, aby zapobiec zamykaniu tej aplikacji przez Wear OS (spróbuj również dezaktywować Play Protect, ponieważ funkcja ta zamyka aplikacje spoza Sklepu Play)
* Czas względny: pokazuje względny czas w minutach od otrzymania ostatniej wartości zamiast znacznika czasu ostatniej wartości. Może to nie działać poprawnie na niektórych zegarkach w związku z optymalizacją baterii systemu Android.
* Inne ustawienia: wszystkie inne ustawienia wprowadza się w aplikacji na telefonie

### Telefon

Ustawienia dla aplikacji na telefon opisane są w samej aplikacji. Ustawienia z telefonu zostaną przesłane do zegarka, jeśli jest on podłączony.

## Źródła

### LibreLink

Aby skonfigurować LibreLink jako „follower”, potrzebne są dane konta z LibreLinkUp.
Jeśli nie skonfigurowałeś jeszcze swojego konta LibreLinkUp, postępuj zgodnie z tą [instrukcją](https://librelinkup.com/articles/getting-started).

Oto krótkie podsumowanie kroków, które należy wykonać:
* otwórz aplikację FreeStyle Libre i wybierz w menu `Udostępnianie` lub `Podłączone aplikacje`.
* aktywuj połączenie LibreLinkUp
* zainstaluj LibreLinkUp ze [Sklepu Play](https://play.google.com/store/apps/details?id=org.nativescript.LibreLinkUp)
* skonfiguruj swoje konto i czekaj na zaproszenie
* po zaakceptowaniu zaproszenia nie potrzebujesz już aplikacji LibreLinkUp.
* teraz możesz dodać swoje konto LibreLinkUp do GlucoDataHandler i aktywować to źródło.

## Widgety
Dostępnych jest kilka rodzajów widgetów na telefon.
Dostępny jest również pływający widget, który może być obsługiwany przez aplikację Tasker.

<img src='images/widgets.jpg' width=200>  <img src='images/pl/FloatingWidget.jpg' width=200>

## Powiadomienia
Dostępne są dwa powiadomienia, które można aktywować. Dla każdego powiadomienia można wybrać ikonę, która pojawi się na pasku stanu telefonu.
Pierwsze powiadomienie jest również używane jako powiadomienie na pierwszym planie, aby zapobiec zamknięciu tej aplikacji przez system Android w tle. 
Jeśli więc masz jakiekolwiek problemy z tą aplikacją, zalecam, aby aktywować przynajmniej pierwsze powiadomienie.
Drugie powiadomienie to puste powiadomienie, które można aktywować, aby uzyskać dodatkową ikonę na pasku stanu.
<figure>
  <img src='images/notifications.jpg' width=200> 
  <figcaption>Pasek stanu pokazuje użycie strzałki trendu i ikon wartości delta obok ikony wartości glukozy z Juggluco.</figcaption>
</figure>

## Komplikacje
Istnieje kilka komplikacji dla różnych typów komplikacji w ramach Wear OS, które mogą wyświetlać:
* Wartość glukozy (używana również do koła zakresu)

<img src='images/complications_glucose1.png' width=200> <img src='images/complications_glucose2.png' width=200>

* Wartość glukozy jako obraz tła (jeśli funkcja ta jest obsługiwana przez tarczę zegarka i zdaje się, że jest dostępna tylko w Wear OS 3)

<img src='images/complications_large_1.png' width=200> <img src='images/complications_large_2.png' width=200>

* Wartość delty (na minutę lub na 5 minut)

<img src='images/complications_delta.png' width=200>

* Tempo (trend) jako wartość i strzałka (strzałka obraca się dynamicznie między +2,0 (↑) a -2,0(↓) i pokazuje podwójne strzałki od +3,0 (⇈) i od -3,0 (⇊))

<img src='images/complications_rate.png' width=200>

* Poziom baterii w zegarku i w telefonie (jeśli jest podłączony)

<img src='images/complications_battery.png' width=200>

**WAŻNA UWAGA:** Nie wszystkie komplikacje są w pełni obsługiwane przez każdą tarczę zegarka. Na przykład typ SHORT_TEXT obsługuje ikonę, tekst i tytuł, ale większość tarcz zegarków pokazuje tylko ikonę i tekst lub tekst i tytuł, ale są też takie, które pokazują wszystkie 3 typy w jednym.
Również komplikacja WARTOŚĆ_ZAKRESU jest obsługiwana inaczej na każdej tarczy zegarka.

## Aplikacja Tasker

-> [Obsługa aplikacji Tasker](./TASKER.md)


# Wesprzyj moją pracę
[🍺 Buy me a beer](https://www.buymeacoffee.com/pachi81)

[Paypal me](https://paypal.me/pachi81)
