/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiladoruami.Final;

/**
 *
 * @author Lety, Cecy & Gab
 */
public class Generador_de_Errores {
    
    public void Errores(String se_esperaba){
        Globales G1 = new Globales();
        switch (se_esperaba){
            
            case "$":
            case "#":
            case "@":
            case "¡":
            case "¿":
            case "?":
            case "~":
            case "^":
            case "[":
            case "]":
                UAMI.errores++;
                UAMI.wr2.append("Tipo de Error:"+ G1.ERROR + ", en la Línea:"+UAMI.linea +": " + G1.TOKEN_INV +": " + se_esperaba + "\n");
                break;
            case "SE CERRO MAL UN COMENTARIO":
                UAMI.errores++;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + (UAMI.linea-1) + "; Se cerro de forma incorrecta el Comentario, tipo de error: " + G1.ERROR + "\n");
                break;
            case "SE CERRO MAL UNA CADENA":
                UAMI.errores++;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + (UAMI.linea-1) + "; Se cerro de forma incorrecta la Cadena, tipo de error: " + G1.ERROR + "\n");
                break;
            case "SE CERRO MAL UN OPERADOR OR":
                UAMI.errores++;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Falta agregar un |, tipo de error: " + G1.ERROR + "\n");
                break;
            case "SE CERRO MAL UN OPERADOR AND":
                UAMI.errores++;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Falta agregar un &, tipo de error: " + G1.ERROR + "\n");
                break;
            case "COMBINACION INCORRECTA DE SIMBOLOS":
                UAMI.errores++;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Combinación de caracteres implicítos inválida tipo de error: " + G1.ERROR + "\n");
                break;
            default:
                //Errores sintácticos
                UAMI.errores++;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Se esperaba: " + se_esperaba +", tipo de error: " + G1.ERROR_S + "\n");
                break;
        }
        
    }
    
}
