# EnchantHighlighter

**Von AbartigerAlex** | Minecraft 1.21.10 | Fabric

Ein clientseitiger Fabric-Mod der Items in Inventaren, Truhen, Verzauberungstischen und Schleifsteinen farbig hervorhebt — basierend auf ihren Verzauberungsfarben.

---

## Features

- **🔴 Red Highlight** – Hebt Items mit roten Enchants hervor
- **🟣 Pink Highlight** – Hebt Items mit pinken/lilanen Enchants hervor (z.B. Lebensraub, Raserei)
- **🟡 Gold Highlight** – Hebt Items mit goldenen Enchants hervor (z.B. Blitz, Köpfung)
- **Pizzastück-Darstellung** – Bei mehreren aktiven Farben wird jede als eigenes Segment eines Kreises dargestellt
- **Suchfunktion** – Filtert Items nach Name oder Lore in allen Inventaren (grün = Treffer, rot = kein Treffer)
- **Persistent** – Alle Einstellungen werden gespeichert und beim nächsten Start wiederhergestellt

## Bedienung

Die Buttons erscheinen **mittig unten** in jedem Inventar-Screen:

```
[ ] Red Enchants    [ ] Pink Enchants    [ ] Gold Enchants
```

- Klick auf einen Button → **aktiviert/deaktiviert** das Highlight (✔ = aktiv)
- Das Suchfeld über den Buttons filtert Items nach Name oder Lore
- Erst ins Suchfeld **klicken**, dann tippen

## Unterstützte Screens

- Truhen (alle Größen)
- Spieler-Inventar
- Verzauberungstisch
- Schleifstein
- Alle anderen `HandledScreen`-basierten Inventare

## Installation

1. [Fabric Loader](https://fabricmc.net/) installieren
2. [Fabric API](https://modrinth.com/mod/fabric-api) in den Mods-Ordner
3. `EnchantHighlighter-1.3.jar` in den Mods-Ordner kopieren
4. Minecraft starten

---

© 2026 AbartigerAlex – MIT License
