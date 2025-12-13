package cr.ac.ufidelitas.proyecto.busnovatech;

/**
 *
 * @author jiro
 */
// Localidad donde el bus pararía
public class Localidad {

    private String nombre;
    private int id;

    public Localidad() {
    }

    public Localidad(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Localidad) {
            return this.id == ((Localidad) obj).id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

