/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.pkg1;

/**
 *
 * @author USER
 */
public class Arista {
    public String destino;
    public Arista siguiente;    
    
    public Arista(String objetivo){
        destino = objetivo;
        siguiente = null;
    }
}
