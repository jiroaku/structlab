package cr.ac.ufidelitas.proyecto.busnovatech;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author lunad
 */

@XmlRootElement(name = "DataSet", namespace = "http://ws.sdde.bccr.fi.cr")
@XmlAccessorType(XmlAccessType.FIELD)

public class IndicadorEconomico {

    @XmlElement(name = "diffgram", namespace = "urn:schemas-microsoft-com:xml-diffgram-v1")
    private Diffgram diffgram;

    // Getters y setters
    public Diffgram getDiffgram() {
        return diffgram;
    }

    public void setDiffgram(Diffgram diffgram) {
        this.diffgram = diffgram;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Diffgram {

        @XmlElement(name = "Datos_de_INGC011_CAT_INDICADORECONOMIC", namespace = "")
        private DatosDeIndicadores datosDeIndicadores;

        // Getters y setters
        public DatosDeIndicadores getDatosDeIndicadores() {
            return datosDeIndicadores;
        }

        public void setDatosDeIndicadores(DatosDeIndicadores datosDeIndicadores) {
            this.datosDeIndicadores = datosDeIndicadores;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DatosDeIndicadores {

        @XmlElement(name = "INGC011_CAT_INDICADORECONOMIC", namespace = "")
        private List<Indicador> indicadores;

        // Getters y setters
        public List<Indicador> getIndicadores() {
            return indicadores;
        }

        public void setIndicadores(List<Indicador> indicadores) {
            this.indicadores = indicadores;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Indicador {

        @XmlElement(name = "COD_INDICADORINTERNO", namespace = "")
        private String codigo;

        @XmlElement(name = "DES_FECHA", namespace = "")
        private String fecha;

        @XmlElement(name = "NUM_VALOR", namespace = "")
        private BigDecimal valor;

        // Getters y setters
        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getFecha() {
            return fecha;
        }

        public void setFecha(String fecha) {
            this.fecha = fecha;
        }

        public BigDecimal getValor() {
            return valor;
        }

        public void setValor(BigDecimal valor) {
            this.valor = valor;
        }
    }
}
