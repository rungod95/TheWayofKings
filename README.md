# ⚔️ Kaladin – The Way of the Kings 🌪️

Videojuego de plataformas 2D desarrollado con **libGDX** como Actividad de Aprendizaje de la 2ª Evaluación de la asignatura *Programación Multimedia y Dispositivos Móviles*.

## 🎮 Instrucciones de juego

- **← / →** : Mover al personaje Kaladin  
- **ESPACIO** : Saltar  
- **ESC** : Pausar el juego  
- **ENTER** : Confirmar selección en los menús  
- **M** : Volver al menú principal desde pantallas de pausa o victoria  
- **R** : Reiniciar nivel (en Game Over o Victoria)  
- **I** : Acceder a las instrucciones desde el menú principal  
- **+ / -** : Subir o bajar el volumen de la música  

---

## 🧩 Características implementadas

- ✅ Menú principal con volumen y dificultad ajustables  
- ✅ Instrucciones accesibles desde el menú  
- ✅ Sistema de pausa completo  
- ✅ Música de fondo y efectos personalizados  
- ✅ Detección de trampas, plataformas móviles, enemigos y pociones  
- ✅ Checkpoints intermedios y finales  
- ✅ Registro de puntuaciones con nombre, tiempo y dificultad  
- ✅ Visualización del Top 10 desde el menú principal  
- ✅ Carga modular de niveles desde archivos `.tmx` sin tocar el código  

---

## 🔧 Tecnologías utilizadas

- 🛠️ **Java 21**
- 🎮 **libGDX**
- 🗺️ **Tiled Map Editor**
- 🔊 **GDX Audio**
- 🔤 **FreeTypeFont** (fuentes personalizadas)

---

## 📁 Estructura del proyecto

```
📦 /core         → Código fuente del juego (lógica, personajes, pantallas)
📦 /lwjgl3       → Launcher principal del juego
📁 /assets       → Recursos: mapas, sonidos, sprites, fuentes
📄 README.md     → Este archivo
```

---

## 🏁 Cómo ejecutar

1. Clona este repositorio.
2. Abre el proyecto con IntelliJ IDEA (u otro IDE compatible con Gradle).
3. Ejecuta el módulo `lwjgl3`.
4. También puedes exportar el `.jar` de escritorio desde `lwjgl3/build/libs`.

---

## 📘 Evaluación y criterios

Este proyecto cumple con los requisitos establecidos para la 2ª evaluación de PMDM:

- ✔️ Gestión de assets, audio y tiles con libGDX  
- ✔️ Integración de TiledMap para niveles modulares  
- ✔️ Sistema de pausa y Game Over  
- ✔️ Sonido y música configurables  
- ✔️ Puntuaciones persistentes con nombre, tiempo y dificultad  
- ✔️ Proyecto subido a GitHub con Wiki, Issues y Releases  
- ✔️ README explicativo y organización en ramas  

---



## 📜 Créditos

- 👨‍💻 **Desarrollador**: Javier Planas – 2º DAM – 2025  
- 🎨 Assets: Recursos pixel art libres o diseñados para este juego  
- 🔊 Sonidos: Librerías de uso libre y creación propia  

---

## 🗂️ Recursos adicionales

- [Wiki del juego](https://github.com/rungod95/TheWayofKings/wiki) (Instrucciones y detalles)
- [Última release]() (Descarga .jar)
- [Sistema de puntuaciones](docs/scores.json)

---

¡Gracias por jugar a *Kaladin – The Way of the Kings*! 🌩️
