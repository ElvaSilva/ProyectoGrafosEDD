/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

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
    
    /** Se agrega un usuario al grafo sin la necesidad de este tener conexiones.
     * 
     * @param elem usuario que se va a a&ntilde;adir al grafo
     */
    public void insertar(String elem){
        NodoGrafo Nodo = new NodoGrafo(elem);
        if (esVacio()){
            pFirst = Nodo;
            pLast = Nodo;
        } else {
            if (tamaño == 1){
                pLast = Nodo;
                pFirst.pnext = pLast;
            } else {
                pLast.pnext = Nodo;
                pLast = Nodo;
            }
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
    
    /** Se elimina el nodo del grafo m&aacute;s no sus conexiones.
     * 
     * @param pNodo nodo a eliminar
     */
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
    
    public Grafo transponer(){
        Grafo traspuesto = new Grafo();
        for (NodoGrafo pAux = pFirst; pAux != null; pAux = pAux.pnext) {
            if (traspuesto.Buscar(pAux.usuario) == null) traspuesto.insertar(pAux.usuario);

            for (Arista pArista = pAux.minilista.primero; pArista != null; pArista = pArista.siguiente) {
                if (!existe_nodo(pArista.destino)) continue; // evita fantasmas
                if (traspuesto.Buscar(pArista.destino) == null) traspuesto.insertar(pArista.destino);
                NodoGrafo dst = traspuesto.Buscar(pArista.destino);
                dst.minilista.insertar_nueva(pAux.usuario); // arista invertida
            }
        }
        return traspuesto;
    }
    
    /** Se van a devolver los usuarios que existen en el grafo.
     * 
     * @return texto
     */
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
    
    /** Se va a devolver cada usuario en el grafo con las conexiones de cada nodo. 
     * Ej: (Nodo A, Nodo B; Nodo A, Nodo F)
     * @return texto
     */
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
    
    public void eliminarAristasHacia(String destino) {
        for (NodoGrafo u = pFirst; u != null; u = u.pnext) {
            if (u.minilista != null) {
                u.minilista.eliminarTodos(destino);
            }
        }
    }

    public void eliminarUsuarioCompleto(String nombre) {
        // primero borra TODAS las aristas que apuntan a 'nombre'
        eliminarAristasHacia(nombre);
        // luego elimina el nodo del listado de nodos
        NodoGrafo n = Buscar(nombre);
        if (n != null) {
            Eliminar(n);
        }
    }
    
    public Grafo clonarProfundo() {
        Grafo copia = new Grafo();

        // Copiar todos los nodos (usuarios)
        for (NodoGrafo n = this.pFirst; n != null; n = n.pnext) {
            copia.insertar(n.usuario);
        }

        // Copiar todas las aristas (relaciones)
        for (NodoGrafo n = this.pFirst; n != null; n = n.pnext) {
            NodoGrafo cn = copia.Buscar(n.usuario);
            for (Arista a = n.minilista.primero; a != null; a = a.siguiente) {
                if (!cn.minilista.Buscar(a.destino)) {
                    cn.minilista.insertar_nueva(a.destino);
                }
            }
        }

        return copia;
    }
}
