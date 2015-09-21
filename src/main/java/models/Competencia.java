package models;

import javax.persistence.*;

@Entity
public class Competencia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nombre;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
