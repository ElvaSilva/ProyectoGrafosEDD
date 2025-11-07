/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */

/** La clase Kosaraju permite guardar los componentes fuertemente conectados, 
 * funciona como una lista de componentes, a su vez, ella va a generar estos 
 * componentes
 */
public class Kosaraju {
    public Componente firstcomp;
    public Componente lastcomp;
    public Grafo grafo;
    public Grafo transpuesto;
    public Pila pila;
    public int tamaño;
    
    
    //Para que esto funcione se debe trasponer el grafo de antemano
    public Kosaraju(Pila Ppila, Grafo pgrafo){
        firstcomp = null;
        lastcomp = null;
        pila = Ppila;
        grafo = pgrafo;
        transpuesto = grafo.transponer();
        tamaño = 0;
    }
    
    /** Se va a recorrer el grafo transpuesto, realizando un DFS en cada nodo, lo
     * que devolver&aacute; todos los nodos que pertenezcan al mismo CFC (Componente 
     * Fuertemente Conectado), esta componente luego ser&aacute; guardado en una 
     * lista de estas mismas.
     */
    public void fuertemente_conectados() {
        // Asegura flags limpios en el transpuesto antes del 2do pase
        transpuesto.establecer_falso();

        while (!pila.esVacio()) {
            // 1) Pop: toma el tope y desapila
            String u = pila.Cima.usuario;
            pila.Desapilar();

            // 2) Busca el nodo en el transpuesto
            NodoGrafo start = transpuesto.Buscar(u);
            if (start == null) {
                // Si por alguna razón no existe en el transpuesto, continúa
                continue;
            }

            // 3) Si no está visitado en el transpuesto, corre DFS y crea componente
            if (!start.visitado) {
                Componente comp = new Componente();
                DFS dfsT = new DFS(transpuesto);
                // Nota: tu SegundoRecorrido no usa realmente el parámetro 'kosaraju' (3er arg).
                // Puedes pasar 'pila' o null; no se utiliza adentro.
                dfsT.SegundoRecorrido(start, comp, pila);

                // Enlazar a la lista de componentes
                if (tamaño == 0) {
                    firstcomp = comp;
                    lastcomp = comp;
                } else {
                    lastcomp.next = comp;
                    lastcomp = comp;
                }
                tamaño++;
            }
        }
    }
}
