package vetgestion.excepcion;

/**
 * Excepcion propia del dominio: se lanza cuando se intenta dispensar o
 * aplicar una cantidad de insumo mayor a la que hay disponible en stock.
 */
public class StockExcepcion extends Exception {

    private static final long serialVersionUID = 1L;

    private final int insumoId;
    private final int disponible;

    public StockExcepcion(String mensaje, int insumoId, int disponible) {
        super(mensaje);
        this.insumoId = insumoId;
        this.disponible = disponible;
    }

    public int getInsumoId() { return insumoId; }
    public int getDisponible() { return disponible; }
}
