<div align="center">

14.0 – Widget Android pentru orele autobuzului

</div>



  14.0 este un widget Android care afișează în timp real următoarele două autobuze disponibile dintr-un set predefinit de ore. Este util pentru utilizatorii care vor să știe rapid când vine următorul autobuz 14L, 14, 14P sau 14M(Multumim otl pentru aplicatia minunata).

##  Funcționalități

- Afișează ora următoarelor două autobuze disponibile
- Actualizare automată la fiecare 15 secunde
- Buton de reîmprospătare manuală
- Suport pentru mai multe variante de autobuz (14L, 14, 14P, 14M)

##  Tehnologii folosite

- **Java** (Android SDK)
- **AppWidgetProvider**
- **AlarmManager**
- **RemoteViews**
- **PendingIntent**

##  Instalare și rulare

1. Clonează repository-ul:
   ```bash
   git clone https://github.com/SicklyC/14.0.git
   ```
1. Deschide proiectul în **Android Studio**.
2. Compilează și instalează aplicația pe un dispozitiv Android sau emulator.
3. Adaugă widgetul **"14.0"** pe ecranul principal al dispozitivului.

##  Structura proiectului

- `NewAppWidget.java` – logica principală a widgetului
- `WidgetUpdateReceiver.java` – gestionarea actualizărilor programate
- `res/layout/new_app_widget.xml` – layout-ul vizual al widgetului
- `AndroidManifest.xml` – declarații pentru widget și receivere

## Logica de funcționare

- Orele autobuzelor sunt stocate într-un array bidimensional.
- Se calculează diferența în minute față de ora curentă.
- Se sortează orele viitoare și se afișează cele mai apropiate două.
- Se actualizează automat la fiecare 15 secunde folosind `Handler`.
