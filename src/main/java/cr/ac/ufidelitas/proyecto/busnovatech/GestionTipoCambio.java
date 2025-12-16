package cr.ac.ufidelitas.proyecto.busnovatech;

import java.math.BigDecimal;
import javax.swing.JOptionPane;

/**
 *
 * @author lunad
 */

public class GestionTipoCambio {

    public void consultarTipoCambio() {
        try {
            ServicioBCCR servicio = new ServicioBCCR(); 

            IndicadorEconomico indicador = servicio.obtenerIndicador(
                    "318", 
                    "15/12/2025",
                    "16/12/2025",
                    "Luna Delgado",
                    "N",
                    "luna.dd26@gmail.com",
                    "9L2D52D2L5" 
            );

            BigDecimal tipoCambio = extraerTipoCambio(indicador);

            if (tipoCambio != null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Tipo de cambio del dólar (BCCR)\n\n₡ " + tipoCambio,
                        "BusNovaTech - Tipo de Cambio",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo obtener el tipo de cambio.",
                        "BusNovaTech - Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Error al consultar el BCCR:\n" + e.getMessage(),
                    "BusNovaTech - Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private BigDecimal extraerTipoCambio(IndicadorEconomico indicadorEconomico) {
        if (indicadorEconomico != null
                && indicadorEconomico.getDiffgram() != null
                && indicadorEconomico.getDiffgram().getDatosDeIndicadores() != null
                && indicadorEconomico.getDiffgram().getDatosDeIndicadores().getIndicadores() != null
                && !indicadorEconomico.getDiffgram().getDatosDeIndicadores().getIndicadores().isEmpty()) {

            return indicadorEconomico
                    .getDiffgram()
                    .getDatosDeIndicadores()
                    .getIndicadores()
                    .get(0)
                    .getValor();
        }
        return null;
    }

}
