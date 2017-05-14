package ar.com.inteligencia.atributo;

import java.util.Arrays;

/**
 * Created by fede on 16/04/17.
 */
public enum Nombre {
    FELIX(0b00), GUSTAVO(0b01), JESUS(0b10), RAMIRO(0b11);

    private final int value;

    Nombre(final int value) {
        this.value = value;
    }

    public static Nombre fromChromosome(boolean[] atributos) {
        int gene0 = atributos[0] ? 1 : 0;
        int gene1 = atributos[1] ? 1 : 0;
        int genes = gene0 | (gene1 << 1);
        return Arrays
                .stream(values())
                .filter(nombre -> nombre.value == genes)
                .findFirst()
                .get();
    }
}
