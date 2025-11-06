/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
public class Grafo {
    public NodoGrafo pFirst;
    public NodoGrafo pLast;
    public int tamaño;
    
    public Grafo(){
        pFirst = null;
        pLast = null;
        tamaño = 0;
    }
    
    public boolean esVacio(){
        return pFirst == null;
    }
    
    public void insertar(String elem){
        NodoGrafo Nodo = new NodoGrafo(elem);
        if (esVacio()){
            pFirst = Nodo;
            pLast = Nodo;
        } else {            
            pLast.pnext = Nodo;
            pLast = Nodo;
            
        }
        tamaño ++;       
    }
    
    public NodoGrafo Buscar(String elem){
        if (esVacio() != true) {
            NodoGrafo pNodo = pFirst;
            while (pNodo != null && !pNodo.usuario.equals(elem)){
                pNodo = pNodo.pnext;
            }
            return pNodo;
        }
        return null;
    }
    
    public void Eliminar(NodoGrafo pNodo){
        NodoGrafo pAux = pFirst;
        if (pNodo != null) {
            if (tamaño == 1) {
                pFirst = null;
                pLast = null;
            } else {
                if (pNodo == pFirst){
                    pFirst = pNodo.pnext;
                    pNodo.pnext = null;
                } else {
                    while (pAux.pnext != pNodo) {
                        pAux = pAux.pnext;
                    }
                    pAux.pnext = pAux.pnext.pnext;
                    if (pNodo == pLast) {
                        pLast = pAux;
                    }
                }
            }
        tamaño --;    
        }
    }
    
    public void establecer_falso(){
        NodoGrafo pAux = pFirst;
        while (pAux != null){
            pAux.visitado = false;
            pAux = pAux.pnext;
        }
    }
    
    public boolean existe_nodo(String elem){
        NodoGrafo pAux = pFirst;
        while (pAux != null){
            if (pAux.usuario.equals(elem)){
                return true;
            }
            pAux = pAux.pnext;
        }
        return false;
    }
    
    public NodoGrafo todos_visitados(){
        NodoGrafo Nodo = pFirst;
        while (Nodo != null){
            if (Nodo.visitado == false){
                return Nodo;
            }
            Nodo = Nodo.pnext;
        }
        return null;
    }
    
    public Grafo transponer(){
        Grafo traspuesto = new Grafo();
        NodoGrafo pAux = pFirst;
        while (pAux != null) {
            // Asegurar que el nodo exista en el traspuesto
            if (traspuesto.Buscar(pAux.usuario) == null) {
                traspuesto.insertar(pAux.usuario);
            }
            Arista pArista = pAux.minilista.primero;
            while (pArista != null) {
                // Asegurar que el nodo destino exista antes de agregar la relación
                if (traspuesto.Buscar(pArista.destino) == null) {
                    traspuesto.insertar(pArista.destino);
                }
                // Ahora sí invertir la arista
                NodoGrafo destinoNodo = traspuesto.Buscar(pArista.destino);
                destinoNodo.minilista.insertar_nueva(pAux.usuario);
                pArista = pArista.siguiente;
            }
            pAux = pAux.pnext;
        }
        return traspuesto;
    }
    
    public String mostrar(){
        NodoGrafo aux = new NodoGrafo("");
        aux = this.pFirst;
        String texto = "";
        while(aux != null){
            texto = texto + aux.usuario + "\n";
            aux = aux.pnext;
        }
        return texto;
    }
    
    public String mostrarRelaciones(){
        NodoGrafo aux = new NodoGrafo("");
        aux = this.pFirst;
        Arista aux2 = new Arista("");
        String texto = "";
        while(aux != null){
            aux2 = aux.minilista.primero;
            while(aux2 != null){
                texto = texto + aux.usuario + ", " + aux2.destino + "\n";
                aux2 = aux2.siguiente;
            }
            aux = aux.pnext;
        }
        return texto;
    }
}
