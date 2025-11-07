/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
public class Pila {
    public NodoGrafo Cima;
    
    public Pila(){
        Cima = null;
    }
    
    public boolean esVacio(){
        return Cima == null;
    }
    
    public void Apilar(String info){
        NodoGrafo pNodo = new NodoGrafo(info);
        pNodo.pnext =  Cima;
        Cima = pNodo;
    }
    
    public void Desapilar(){
        if (Cima != null){
            NodoGrafo pTemp = Cima;
            Cima = pTemp.pnext;
        }
    }
    
    public void eliminar(String elem){
        NodoGrafo pNodo = Cima;
        if (pNodo.usuario.equals(elem)){
            Cima = pNodo.pnext;
        } else {
            Desapilar();
            eliminar(elem);
            Apilar(pNodo.usuario);
        }
    }
    
}
