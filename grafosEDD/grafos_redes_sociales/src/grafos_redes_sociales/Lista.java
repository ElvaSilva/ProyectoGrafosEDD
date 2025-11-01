/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grafos_redes_sociales;

/**
 *
 * @author elva
 */
public class Lista {
    Nodo raiz;
    int size;

    public Lista(Nodo raiz) {
        this.raiz = raiz;
        this.size = 1;
    }
    
    public Lista(){
        this.raiz = null;
        this.size = 0;
    }
    
    public void insertar(Nodo aux){
        if (this.raiz == null){
            this.raiz = aux;
        }
        Nodo aux2 = new Nodo();
        aux2 = this.raiz;
        while(aux2.vSig != null){
            aux2 = aux2.vSig;
        }
        aux2.vSig = aux;
    }
    
}
