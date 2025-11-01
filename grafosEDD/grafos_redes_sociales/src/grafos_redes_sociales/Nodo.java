/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package grafos_redes_sociales;

/**
 *
 * @author elva
 */
public class Nodo {
    String username;
    Lista vecinos;
    Nodo vSig;

    public Nodo() {
        this.username = null;
        this.vecinos = null;
        this.vSig = null;
    }
    
    public Nodo(String username) {
        this.username = username;
        this.vecinos = null;
        this.vSig = null;
    }
    
    /**
     * Conecta a un nodo "A" con uno "B". La conexi&oacute;n va exclusivamente
     * de "A" a "B".
     * @param aux "A"
     * @param aux2 "B"
     */
    public void conectar(Nodo aux, Nodo aux2){
        aux.vecinos.insertar(aux2);
    }
    
}
