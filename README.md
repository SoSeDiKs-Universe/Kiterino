[All The Forks](https://gist.github.com/SoSeDiK/1773ef2c239722f7083a25b2f78619ed)

<div align="center">

## Kiterino

Kiterino is a [Purpur](https://github.com/PurpurMC/Purpur) fork maintained specifically for SoSeDiK's Universe.

</div>

## Building and setting up

Run the following commands in the root directory:

```
./gradlew applyPatches
./gradlew rebuildPatches
./gradlew createReobfPaperclipJar
```

Setup dev bundle:

```
./gradlew publishToMavenLocal -PpublishDevBundle
```

License
---

[![MIT License](https://img.shields.io/badge/licence-MIT-brightgreen)](LICENSE)

All of the original Kiterino patches are licensed under MIT found [here](LICENSE).
