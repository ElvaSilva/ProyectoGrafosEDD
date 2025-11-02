/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
public class Componente {
    public Arista primero;
    public Arista ultimo;
    public Componente next;
    
    public Componente() {
        primero = null;
        ultimo = null;
        next = null;
    }
    
    public boolean esVacio(){
        return primero == null;
    }
    
    public void insertar(String destino){
        Arista pArista = new Arista(destino);
        if (esVacio()){
            primero = pArista;
            ultimo = pArista;
        } else if (primero == ultimo) {
            ultimo = pArista;
            primero.siguiente = ultimo;
        } else {
            ultimo.siguiente = pArista;
            ultimo = pArista;
        }
    }
}
