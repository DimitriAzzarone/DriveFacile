# Drive Facile

Prototipo Android per scegliere uno o più file dal telefono/tablet e inviarli a Google Drive.

Il progetto segue lo stesso metodo usato per `DimitriAzzarone/chatgpt-messenger` e Luminex Browser: Java + XML, repository dedicato e APK compilato nel cloud con GitHub Actions.

## Come funziona

1. Tocca **Scegli i file**.
2. Seleziona uno o più documenti nel selettore Android.
3. Tocca **Carica su Drive**.
4. Scegli Google Drive nel pannello di condivisione e indica la cartella.

L'app non richiede accesso completo alla memoria: Android concede soltanto la lettura dei file scelti.

## Compilazione interamente dal browser con GitHub

1. Crea un repository GitHub vuoto chiamato `DriveFacile`.
2. Carica nella radice del repository tutto il contenuto di questa cartella.
3. Apri la scheda **Actions** del repository.
4. Apri **Compila APK** e premi **Run workflow**.
5. Al termine apri l'esecuzione e scarica l'artefatto `DriveFacile-debug`.
6. Estrai lo ZIP e installa `app-debug.apk` sul dispositivo Android.

La compilazione viene eseguita online da GitHub Actions: non servono Termux, Android Studio o un computer.

## Stato del prototipo

- Selezione multipla di qualunque tipo di file.
- Elenco dei nomi selezionati.
- Invio sicuro tramite pannello Android verso Google Drive.
- Interfaccia in italiano.
- Compatibile da Android 6 (API 23).

## Passo successivo possibile

Integrare Google Drive API per accesso all'account, scelta della cartella dentro l'app, barra di avanzamento e caricamento automatico. Questa modalità richiede la configurazione OAuth in Google Cloud.
