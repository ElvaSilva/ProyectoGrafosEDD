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
   
    public void insertar_nueva(String destino){
        if (!(Buscar(destino))){
            Arista pArista = new Arista(destino);
            if (esVacio()){
                primero = pArista;
                ultimo = pArista;
            } else {
                ultimo.siguiente = pArista;
                ultimo = pArista;
            }
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
           
            if (primero == ultimo && primero.destino.equals(objetivo)){
                primero = null;
                ultimo = null;
            } 
            else if (primero.destino.equals(objetivo)){
                primero = primero.siguiente;
            } 
            else {
                Arista pArista = primero;
                while (pArista.siguiente != null && 
                       !pArista.siguiente.destino.equals(objetivo)){
                    pArista = pArista.siguiente;
                }
                // Si el nodo a eliminar es el último
                if (pArista.siguiente == ultimo){
                    ultimo = pArista;
                    ultimo.siguiente = null;
                } 
                else if (pArista.siguiente != null){
                    pArista.siguiente = pArista.siguiente.siguiente;
                }
            }
        }
    }
}
