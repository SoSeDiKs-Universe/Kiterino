<img src="https://avatars.githubusercontent.com/u/68168320?s=256" alt="Happy Moople" align="right">
<div align="center">

  ## Kiterino 🐮

  Kiterino is a [Purpur](https://github.com/PurpurMC/Purpur) fork maintained specifically for [SoSeDiK's Universe](https://sosedik.com).

  It contains different questionable experimental APIs that should not be used normally.
  
  There are also hardcoded vanilla mechanics changes that were considered suitable for our server.
  
  [![Made with Love](https://img.shields.io/badge/made%20with-Love%20%E2%9D%A4%EF%B8%8F-%23ffcdd2)]() [![and Milk](https://img.shields.io/badge/combined%20with-Milk%20%F0%9F%A5%9B-%23e1bee7)]() [![and Milk](https://img.shields.io/badge/and%20lots%20of-Cookies%20%F0%9F%8D%AA-%23bbdefb)]()

</div>

## Building and setting up

Run the following commands in the root directory:

```
./gradlew applyPatches
./gradlew createReobfPaperclipJar
```

Publishing the dev bundle:

```
./gradlew publishToMavenLocal -PpublishDevBundle
```

For anything else you can refer to [Paper](https://github.com/PaperMC/Paper)'s documentation on how to contribute.

License
---

[![MIT License](https://img.shields.io/badge/licence-MIT-brightgreen)](LICENSE)

All of the original Kiterino patches are licensed under MIT found [here](LICENSE).
