/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladoruami.Final;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Cecy, Lety & Gab
 */
public class Alex {

    public static class A_Buffer {

        public int pos_leida;
        public int tamaño;
        public String cadena;
    }

    public static A_Buffer Buffer;
    public static BufferedReader b;
    //public static int bandera = 0;

    public Alex(FileReader Archivo_fte) {
        Buffer = new A_Buffer();
        b = new BufferedReader(Archivo_fte);
    }

    public int ALexico(Globales g, Tabla_de_Simbolos t) throws IOException {
        Globales G = g;
        String Lexbuf = "";
        char c;
        c = LeeCaracter();
        /*Gramatica que ignora los espacios, tabulaciones y los comentarios*/
        while (c == ' ' || c == '\t' || c == '{') {
            if (c == '{') {
                while (c != '}' && Buffer.pos_leida != Buffer.tamaño) {
                    Lexbuf = Lexbuf + c;
                    c = LeeCaracter();
                }
                if (c == '}') {
                    return ALexico(g, t);              
                }
                if (Buffer.pos_leida == Buffer.tamaño) {
                    UAMI.errores++;
                    UAMI.linea++;
                    UAMI.tokenval = Lexbuf;
                    UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Se cerro de forma incorrecta el Comentario, tipo de error: " + G.ERROR + "\n");
                    return ALexico(g, t);              
                }
            }
            if (Buffer.pos_leida == Buffer.tamaño) {
                UAMI.linea++;
            }
            c = LeeCaracter();
        }
        /*Gramatica que reconoce cadenas*/
        if (c == '"') {
            Lexbuf = Lexbuf + c;
            c = LeeCaracter();
            while (c != '"' && Buffer.pos_leida != Buffer.tamaño) {
                Lexbuf = Lexbuf + c;
                c = LeeCaracter();
            }
            if (c == '"') {
                Lexbuf = Lexbuf + c;
                int pos;
                pos = t.Buscar_Simbolo(Lexbuf);
                if (pos != -1) {
                    return pos;
                } else {
                    pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.CADENA);
                    return pos;
                }
            }
            if (Buffer.pos_leida == Buffer.tamaño) {
                UAMI.errores++;
                UAMI.linea++;
                UAMI.tokenval = Lexbuf;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Se cerro de forma incorrecta la cadena, tipo de error: " + G.ERROR + "\n");
                return ALexico(g, t);              
            }
        }
        /*Gramatica que reconoce numeros enteros*/
        if (Character.isDigit(c)) {
            Lexbuf = "";
            while (Character.isDigit(c)) {
                Lexbuf = Lexbuf + c;
                c = LeeCaracter();
            }
            Deslee();
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.NUM_ENT);
                return pos;
            }
            
        }
        /*Gramatica que reconoce los Identificadores o las palabras reservadas*/
        if (Character.isAlphabetic(c) || c == '_') {
            Lexbuf = "";
            int p;
            while (Character.isDigit(c) || c == '_' || Character.isAlphabetic(c)) {
                Lexbuf = Lexbuf + c;
                c = LeeCaracter();
            }
            Deslee();
            p = t.Buscar_Simbolo(Lexbuf);
            if (p != -1) {
                return p;
            } else {
                p = t.Inserta_en_Tabla_Simbolos(Lexbuf, g.ID);
                return p;
            }
        }
        /*Gramatica que reconoce asignaciones o comparaciones*/
        if (c == '=') {
            Lexbuf = Lexbuf + String.valueOf(c);
            c = LeeCaracter();
            if (c == '=') {
                Lexbuf = Lexbuf + String.valueOf(c);
                int pos;
                pos = t.Buscar_Simbolo(Lexbuf);
                if (pos != -1) {
                    return pos;
                } else {
                    pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.RELOP);
                    return pos;
                }
                
            }
            Deslee();
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.ASIGNACION);
                return pos;
            }
           
        }
        /*Gramaticas que reconocen los Operadores Lógicos*/
        if (c == '<') {
            Lexbuf = Lexbuf + String.valueOf(c);
            c = LeeCaracter();
            if (c == '+' || c == '-' || c == '/' || c == '*' || c == '%' || c == '!') {
                Lexbuf = Lexbuf + String.valueOf(c);
                UAMI.tokenval = Lexbuf;
                UAMI.errores++;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Combinación de caracteres implicítos inválida tipo de error: " + G.ERROR + "\n");
                
                return -3;             
            } else {
                if (c == '=') {
                    Lexbuf = Lexbuf + String.valueOf(c);
                    int pos;
                    pos = t.Buscar_Simbolo(Lexbuf);
                    if (pos != -1) {
                        return pos;
                    } else {
                        pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.RELOP);
                        return pos;
                    }
                }
            }
            Deslee();
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.RELOP);
                return pos;
            }
            
        }
        if (c == '>') {
            Lexbuf = Lexbuf + String.valueOf(c);
            c = LeeCaracter();
            if (c == '+' || c == '-' || c == '/' || c == '*' || c == '%' || c == '!') {
                Lexbuf = Lexbuf + String.valueOf(c);
                UAMI.tokenval = Lexbuf;
                UAMI.errores++;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Combinación de caracteres implicítos inválida tipo de error: " + G.ERROR + "\n");
                
                return -3;
            } else {
                if (c == '=') {
                    Lexbuf = Lexbuf + String.valueOf(c);
                    int pos;
                    pos = t.Buscar_Simbolo(Lexbuf);
                    if (pos != -1) {
                        return pos;
                    } else {
                        pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.RELOP);
                        return pos;
                    }
                    
                }
            }
            Deslee();
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.RELOP);
                return pos;
            }
          
        }
        /*Gramatica que reconoce && o ||*/
        if (c == '|') {
            Lexbuf = Lexbuf + String.valueOf(c);
            c = LeeCaracter();
            if (c == '|') {
                Lexbuf = Lexbuf + String.valueOf(c);
                int pos;
                pos = t.Buscar_Simbolo(Lexbuf);
                if (pos != -1) {
                    return pos;
                } else {
                    pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.LOGOP);
                    return pos;
                }
            }
            Deslee();
            UAMI.errores++;
            UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Falta agregar un |, tipo de error: " + G.ERROR + "\n");
            return -3;             
        }
        if (c == '&') {
            Lexbuf = Lexbuf + String.valueOf(c);
            c = LeeCaracter();
            if (c == '&') {
                Lexbuf = Lexbuf + String.valueOf(c);
                int pos;
                pos = t.Buscar_Simbolo(Lexbuf);
                if (pos != -1) {
                    return pos;
                } else {
                    pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.LOGOP);
                    return pos;
                }
                
            }
            Deslee();
            UAMI.errores++;
            UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Falta agregar un &, tipo de error: " + G.ERROR + "\n");
            return -3;
        }
        /*Gramatica que reconoce distinto o negación*/
        if (c == '!') {
            Lexbuf = Lexbuf + String.valueOf(c);
            c = LeeCaracter();
            if (c == '+' || c == '-' || c == '/' || c == '*' || c == '%' || c == '<' || c == '>') {
                Lexbuf = Lexbuf + String.valueOf(c);
                UAMI.tokenval = Lexbuf;
                UAMI.errores++;
                UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; Combinación de caracteres implicítos inválida tipo de error: " + G.ERROR + "\n");
                
                return -3;
            } else {
                if (c == '=') {
                    Lexbuf = Lexbuf + String.valueOf(c);
                    int pos;
                    pos = t.Buscar_Simbolo(Lexbuf);
                    if (pos != -1) {
                        return pos;
                    } else {
                        pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.RELOP);
                        return pos;
                    }
                    }
            }
            Deslee();
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.LOGOP);
                return pos;
            }
        }
        /*Gramaticas que reconocen operaciones*/
        if (c == '+') {
            Lexbuf =String.valueOf(c);
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.ADDOP);
                return pos;
            }
            
        }
        if (c == '*') {
            Lexbuf =String.valueOf(c);
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.MULOP);
                return pos;
            }
            
        }
        if (c == '/') {
            Lexbuf =String.valueOf(c);
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.MULOP);
                return pos;
            }
            
        }
        if (c == '-') {
            Lexbuf =String.valueOf(c);
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.ADDOP);
                return pos;
            }
            
        }
        if (c == '%') {
            Lexbuf =String.valueOf(c);
            int pos;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.MODULO);
                return pos;
            }
            
        }
        /*Gramatica que reconoce los token's invalidos*/
        if (c == '#' || c == '@' || c == '$' || c == '?' || c == '¿' || c == '^' || c == '~' || c == ']' || c == '[') {
            UAMI.errores++;
            UAMI.wr2.append("Error " + UAMI.errores + " en la linea: " + UAMI.linea + "; TOKEN INVALIDO: " + c + "\n");
            return ALexico(g, t);
        }
        /*Gramatica que reconoce final de archivo*/
        if (c == '\0') {
            Lexbuf="FIN";
            int pos=0;
            pos = t.Buscar_Simbolo(Lexbuf);
            if (pos != -1) {
                return pos;
            } else {
                pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.HECHO);
                return pos;
            }
        }
        int pos;
        Lexbuf+=c;
        pos = t.Buscar_Simbolo(Lexbuf);
        if (pos != -1) {
            return pos;
        } else {
            pos = t.Inserta_en_Tabla_Simbolos(Lexbuf, G.RESTO_MUNDO);
            return pos;
        }
    }

    private char LeeCaracter() throws IOException {
        char c;
        if (Buffer.pos_leida >= Buffer.tamaño) {
            Llena_Buffer();
        }
        if (Buffer.tamaño != 0) {
            c = Buffer.cadena.charAt(Buffer.pos_leida);
            Buffer.pos_leida++;
            return c;
        } else {
            return '\0';
        }
    }

    public void Llena_Buffer() throws FileNotFoundException, IOException {
        String Cadena;
        Cadena = b.readLine() + " ";
        if (!Cadena.equals("null ") && !Cadena.equals("NULL ") && !Cadena.equals("null") && !Cadena.equals("NULL")) {
            Buffer.pos_leida = 0;
            Buffer.cadena = Cadena;
            Buffer.tamaño = Cadena.length();
        } else {
            Buffer.pos_leida = 0;
            Buffer.cadena = null;
            Buffer.tamaño = 0;
        }
    }

    private void Deslee() {
        Buffer.pos_leida--;
    }
}