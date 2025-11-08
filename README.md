# Proyecto 1 – Análisis de Redes Sociales

**Materia:** Estructura de Datos  
**Grupo:** Sección 2526-1  
**Integrantes:**
- Remo Agostinelli  
- Christian Sanchez  
- Elva Silva  

**Lenguaje:** Java  
**Entorno:** NetBeans 27 / JDK 25  
**Fecha:** 7 de noviembre de 2025  
**Repositorio:** https://github.com/ElvaSilva/ProyectoGrafosEDD.git

---

## 📘 Descripción General

Este proyecto implementa un **analizador de redes sociales dirigido**, donde cada usuario se representa como un **nodo** y las relaciones de seguimiento como **aristas dirigidas**.  
El sistema identifica los **Componentes Fuertemente Conexos (CFC)** utilizando el **algoritmo de Kosaraju**, mostrando gráficamente los grupos de usuarios que se siguen mutuamente.

La aplicación incluye una **interfaz gráfica (Swing + GraphStream)** que permite:
- Cargar y visualizar el grafo desde un archivo de texto (`usuarios.txt`).
- Agregar y eliminar usuarios o relaciones.
- Guardar los cambios y reiniciar el grafo al estado base.
- Identificar y colorear los CFC encontrados.
- Recibir advertencias antes de perder datos no guardados.
- Auto-cargar el archivo base al iniciar el programa.

---

## 🗂️ Estructura del Proyecto

```plaintext
proyecto1/
└── src/proyecto/pkg1/
    ├── Proyecto1.java              # Clase principal
    ├── Interfaz.java               # Interfaz gráfica con JFrame y GraphStream
    ├── ManejoArchivos.java         # Lectura y escritura del archivo de usuarios
    ├── Grafo.java                  # Implementación del TDA Grafo
    ├── NodoGrafo.java              # Nodo individual del grafo
    ├── Arista.java                 # Representación de relación dirigida
    ├── ListaAdyacencia.java        # Lista de aristas por nodo
    ├── ListaCadena.java            # Lista auxiliar de strings
    ├── DiccionarioStringInt.java   # Asigna índices de CFC
    ├── Componente.java             # Representa un componente fuertemente conexo
    ├── DFS.java                    # Primer recorrido de Kosaraju
    ├── Kosaraju.java               # Algoritmo principal de CFC
    ├── Pila.java                   # Pila usada en DFS y Kosaraju
    └── usuarios.txt                # Archivo de datos de ejemplo
```

---

## ⚙️ Requerimientos del Proyecto

## ✅ Requerimientos del Proyecto

### 🔹 Requerimientos Funcionales

| Código | Descripción | Estado |
|:--:|:--|:--:|
| **A** | **Cargar archivo:** El usuario puede seleccionar un archivo `.txt` mediante `JFileChooser`. Si hay datos sin guardar, se muestra una alerta antes de reemplazar el grafo en memoria. | ✅ (`ManejoArchivos` + `Interfaz`) |
| **B** | **Modificar grafo:** El usuario puede agregar o eliminar usuarios, así como crear o eliminar relaciones entre ellos directamente desde la interfaz. | ✅ (`Interfaz` + `Grafo`) |
| **C** | **Actualizar repositorio:** Permite guardar los cambios realizados (usuarios o relaciones) directamente en el archivo de texto, conservando el formato original. Además, al iniciar el programa se autocarga `usuarios.txt`. | ✅ (`ManejoArchivos.actualizar_archivo` + autocarga inicial) |
| **D** | **Mostrar grafo:** Visualiza el grafo en una interfaz Swing embebida usando GraphStream, con soporte para reiniciar la vista y ajustar el zoom. | ✅ (`GraphStream + SwingViewer`) |
| **E** | **Componentes fuertemente conexos (CFC):** Aplica el algoritmo de **Kosaraju (DFS)** y colorea cada componente con un color distinto. Si no existen CFC no triviales, se notifica al usuario. | ✅ (`Kosaraju` + `Interfaz.visualizarCFC()`) |

---

### 🔹 Requerimientos Técnicos

| # | Descripción | Estado |
|:--:|:--|:--:|
| **1** | Implementación basada en **grafo dirigido** utilizando **lista de adyacencia**. | ✅ |
| **2** | No se utilizan librerías externas para el TDA. Solo se emplea **GraphStream** para la representación visual. | ✅ |
| **3** | Toda la interacción es **gráfica (Swing)**, sin uso de consola. | ✅ |
| **4** | El sistema **carga y guarda datos desde un archivo de texto** usando `JFileChooser`. | ✅ |
| **5** | Documentación con **Javadoc** en todas las clases públicas. | ✅ |
| **6** | Diagrama de clases detallado incluido en el informe final. | ✅ |

---

💡 **Conclusión:**  
Todos los requerimientos funcionales y técnicos establecidos en el enunciado han sido implementados y verificados.  
El programa ofrece una experiencia visual completa, segura frente a pérdida de datos, y cumple estrictamente con las restricciones del proyecto (sin librerías externas para el TDA, grafo dirigido, GUI 100% Swing).

---

## 🖥️ Instrucciones de Uso

1. **Ejecutar el programa**
   - Ejecutar `Proyecto1.java` desde NetBeans o mediante terminal (`java -jar Proyecto1.jar`).

2. **Cargar un grafo**
   - El programa carga automáticamente `usuarios.txt` si está en la carpeta raíz del proyecto.
   - También puede cargarse manualmente desde el botón **“Cargar Archivo”**.

3. **Editar el grafo**
   - **Agregar Usuario:** crea un nuevo nodo (@usuario).
   - **Agregar Relación:** conecta dos usuarios (dirigida).
   - **Eliminar Usuario:** elimina el nodo y todas sus relaciones.

4. **Guardar y reiniciar**
   - **Actualizar Repositorio:** guarda los cambios.
   - **Reiniciar Grafo:** vuelve al último estado guardado.
   - **Advertencia:** se muestra al intentar cerrar o cambiar de archivo con datos sin guardar.

5. **Mostrar CFC (Kosaraju)**
   - Calcula los **componentes fuertemente conexos** y los colorea en la visualización.
   - Muestra un mensaje con la lista de CFC encontrados.
   - Si no hay CFC no triviales, la interfaz lo notifica claramente.

---

## 🧠 Algoritmo Implementado

**Algoritmo de Kosaraju-Sharir:**

1. Se realiza un **DFS** sobre el grafo original y se guarda el orden de finalización en una pila.  
2. Se invierte la dirección de todas las aristas.  
3. Se hace un nuevo DFS siguiendo el orden inverso, identificando los CFC.

---

## 💾 Formato del archivo `usuarios.txt`

```txt
usuarios
@pepe
@mazinger
@juanc
@xoxojaime
@tuqui33
relaciones
@pepe, @mazinger
@mazinger, @juanc
@juanc, @pepe
@xoxojaime, @tuqui33

