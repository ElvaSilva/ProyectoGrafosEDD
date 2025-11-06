/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
public class ListaAdyacencia {
    
    public Arista primero;
    public Arista ultimo;
    
    public ListaAdyacencia(){
        primero = null;
        ultimo = null;
    }
    
    public boolean esVacio(){
        return primero == null; 
    }
    /** Con el usuario proporcionado, se va a agregar este usuario a la lista de
     * conexiones del nodo en el que se llama el m&eacute;todo.
     * @param destino nombre del usuario que se quiere añadir 
     */
    public void insertar_nueva(String destino){
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
    
    public boolean Buscar(String objetivo){
        Arista pArista = primero;
        while (pArista != null){
            if (pArista.destino.equals(objetivo)){
                return true;
            }
            pArista = pArista.siguiente;
        }
        return false;
    }
    
    public void Eliminar(String objetivo){
        if (Buscar(objetivo)){
            if (primero.destino.equals(objetivo) && ultimo.destino.equals(objetivo)){
                primero = null;
                ultimo = null;
            } else if (primero.destino.equals(objetivo)){
                primero = primero.siguiente;
            } else {
                Arista pArista = primero;
                while (!(pArista.siguiente.destino.equals(objetivo))){
                    pArista = pArista.siguiente;
                }
                if (pArista.siguiente != null) {
                    pArista.siguiente = pArista.siguiente.siguiente;
                }
            }
        }
    }
}
