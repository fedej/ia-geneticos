package ar.com.inteligencia;

import ar.com.inteligencia.atributo.Apellido;
import ar.com.inteligencia.atributo.EstadoCivil;
import lombok.RequiredArgsConstructor;
import org.jenetics.BitGene;
import org.jenetics.Genotype;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
class Reglas {

    private Optional<Sospechoso> felix = Optional.empty();
    private Optional<Sospechoso> gustavo = Optional.empty();
    private Optional<Sospechoso> jesus = Optional.empty();
    private Optional<Sospechoso> ramiro = Optional.empty();

    Reglas(Genotype<BitGene> genotype) {

        genotype.stream()
                .map(Sospechoso::new)
                .forEach(s -> {
                    switch (s.getNombre()) {
                        case FELIX:
                            felix = Optional.of(s);
                            break;
                        case GUSTAVO:
                            gustavo = Optional.of(s);
                            break;
                        case JESUS:
                            jesus = Optional.of(s);
                            break;
                        case RAMIRO:
                            ramiro = Optional.of(s);
                            break;
                    }
                });

    }

    private static final double FAIL_SCORE = -2;
    private static final double PUNISH_SCORE = -10;
    public static final double SUCCESS_SCORE = 10;

    // 1
    double _1_felixMasJovenQueAbad() {
        if (!felix.isPresent()) {
            return PUNISH_SCORE;
        }

        Optional<Sospechoso> abad = getByApellido(Apellido.ABAD);
        return abad
                .map(sospechoso -> sospechoso.getEdad() > felix.get().getEdad() ? SUCCESS_SCORE : FAIL_SCORE)
                .orElse(PUNISH_SCORE);
    }

    // 2
    double _2_viudoY50SonInocentes() {
        Optional<Sospechoso> viudo = getByEstadoCivil(EstadoCivil.VIUDO);
        Optional<Sospechoso> sospDe50 = getByEdad(50);

        if (!viudo.isPresent() || !sospDe50.isPresent()) {
            return PUNISH_SCORE;
        } else {
            return viudo.get().isInocente() && sospDe50.get().isInocente() ? SUCCESS_SCORE : FAIL_SCORE;
        }
    }

    // 3
    double _3_asesinoYDivorciadoAcusanAGustavo() {
        Optional<Sospechoso> divorciado = getByEstadoCivil(EstadoCivil.DIVORCIADO);
        Optional<Sospechoso> asesino = getAsesino();

        if (!divorciado.isPresent() || !asesino.isPresent()) {
            return PUNISH_SCORE;
        } else {
            return divorciado.get().isAcusoAGustavo() && asesino.get().isAcusoAGustavo() ? SUCCESS_SCORE : FAIL_SCORE;
        }
    }

    // 4
    double _4_casadoQuiereASuMujer() {
        Optional<Sospechoso> casado = getByEstadoCivil(EstadoCivil.CASADO);
        return casado.map(sospechoso -> sospechoso.isQuiereASuMujer() ? SUCCESS_SCORE : FAIL_SCORE).orElse(PUNISH_SCORE);
    }

    // 5
    double _5_primos() {
        Optional<Sospechoso> elDe42 = getByEdad(42);
        Optional<Sospechoso> elDe44 = getByEdad(44);

        if (!elDe42.isPresent() || !elDe44.isPresent()) {
            return PUNISH_SCORE;
        } else {
            return (elDe42.get().isPrimosEnParis() && elDe44.get().isPrimosEnLondres()) ? SUCCESS_SCORE : FAIL_SCORE;
        }

    }

    // 6
    double _6_solteroMasJovenQueFelix() {
        if (!felix.isPresent()) {
            return PUNISH_SCORE;
        }

        Optional<Sospechoso> soltero = getByEstadoCivil(EstadoCivil.SOLTERO);
        return soltero
                .map(sospechoso -> sospechoso.getEdad() < felix.get().getEdad() ? SUCCESS_SCORE : FAIL_SCORE)
                .orElse(PUNISH_SCORE);
    }

    // 7
    double _7_jesusDomino() {

        if (!felix.isPresent()) {
            return PUNISH_SCORE;
        }

        if (!gustavo.isPresent()) {
            return PUNISH_SCORE;
        }

        if (!jesus.isPresent()) {
            return PUNISH_SCORE;
        }

        if (!ramiro.isPresent()) {
            return PUNISH_SCORE;
        }


        return jesus.get().isAficionadoAlDomino()
                && !felix.get().isAficionadoAlDomino()
                && !gustavo.get().isAficionadoAlDomino()
                && !ramiro.get().isAficionadoAlDomino() ? SUCCESS_SCORE : FAIL_SCORE;
    }

    // 8
    double _8_abadY54Inocentes() {
        Optional<Sospechoso> abad = getByApellido(Apellido.ABAD);
        Optional<Sospechoso> sospDe54 = getByEdad(54);

        if (!abad.isPresent() || !sospDe54.isPresent()) {
            return PUNISH_SCORE;
        } else {
            return abad.get().isInocente() && sospDe54.get().isInocente() ? SUCCESS_SCORE : FAIL_SCORE;
        }
    }

    // 9
    double _9_gustavoMayorQueViudo() {
        if (!gustavo.isPresent()) {
            return PUNISH_SCORE;
        }

        Optional<Sospechoso> viudo = getByEstadoCivil(EstadoCivil.VIUDO);
        return viudo
                .map(sospechoso -> sospechoso.getEdad() < gustavo.get().getEdad() ? SUCCESS_SCORE : FAIL_SCORE)
                .orElse(PUNISH_SCORE);
    }

    // 10
    double _10_villarCampeonBillar() {
        Optional<Sospechoso> villar = getByApellido(Apellido.VILLAR);
        return villar
                .map(sospechoso -> sospechoso.isCampeonDeBillar() ? SUCCESS_SCORE : FAIL_SCORE)
                .orElse(PUNISH_SCORE);
    }

    // 11
    double _11_cerveraBisiesto() {
        Optional<Sospechoso> cervera = getByApellido(Apellido.CERVERA);
        Optional<Sospechoso> lamata = getByApellido(Apellido.LAMATA);
        Optional<Sospechoso> abad = getByApellido(Apellido.ABAD);
        Optional<Sospechoso> villar = getByApellido(Apellido.VILLAR);

        boolean estan = cervera.isPresent() && lamata.isPresent() && abad.isPresent() && villar.isPresent();

        if (!estan) {
            return PUNISH_SCORE;
        } else {
            return cervera.get().isNacioEnAnioBisiesto() &&
                    !lamata.get().isNacioEnAnioBisiesto() &&
                    !abad.get().isNacioEnAnioBisiesto() &&
                    !villar.get().isNacioEnAnioBisiesto() ? SUCCESS_SCORE * 2 : FAIL_SCORE * 2;
        }

    }

    // 12
    double _12_ramiroMasJovenQueLamata() {
        if (!ramiro.isPresent()) {
            return PUNISH_SCORE;
        }

        Optional<Sospechoso> lamata = getByApellido(Apellido.LAMATA);
        return lamata.map(sospechoso -> sospechoso.getEdad() > ramiro.get().getEdad() ? SUCCESS_SCORE * 5 : FAIL_SCORE * 6).orElse(PUNISH_SCORE);
    }

    // Restricciones
    boolean _13_unSoloAsesino() {
        long collect = sospechosos().filter(Sospechoso::isInocente).count();
        return collect == 3L;
    }

    boolean _14_unSoloEstadoCivil() {
        long collect = sospechosos().map(Sospechoso::getEstadoCivil).distinct().count();
        return collect == 4L;
    }

    boolean _15_unSoloApellido() {
        long collect = sospechosos().map(Sospechoso::getApellido).distinct().count();
        return collect == 4L;
    }

    boolean _15_unSoloNombre() {
        long collect = sospechosos().map(Sospechoso::getNombre).distinct().count();
        return collect == 4L;
    }

    boolean _regla1Solo(Function<Sospechoso, ?> mapper) {
        return sospechosos().map(mapper).distinct().count() == 4L;
    }

    boolean _16_unSoloEdad() {
        long collect = sospechosos().map(Sospechoso::getEdad).distinct().count();
        return collect == 4L;
    }

    boolean _17_unSoloProp() {
        int sum = sospechosos().map(s -> (s.isAcusoAGustavo() ? 1 : 0)
                + (s.isAcusoAGustavo2() ? 1 : 0)
                + (s.isCampeonDeBillar() ? 1 : 0)
                + (s.isPrimosEnLondres() ? 1 : 0)
                + (s.isPrimosEnParis() ? 1 : 0)
                + (s.isAficionadoAlDomino() ? 1 : 0)
                + (s.isNacioEnAnioBisiesto() ? 1 : 0)
                + (s.isQuiereASuMujer() ? 1 : 0)).mapToInt(Integer::intValue).sum();

        if (sum == 8) {
            System.out.println("hola");
        }

        return sum == 8;
    }

    private Stream<Sospechoso> sospechosos() {
        return Stream.of(felix, gustavo, jesus, ramiro)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<Sospechoso> getByEstadoCivil(EstadoCivil estadoCivil) {
        return sospechosos()
                .filter(s -> estadoCivil.equals(s.getEstadoCivil()))
                .findFirst();
    }

    private Optional<Sospechoso> getByApellido(Apellido apellido) {
        return sospechosos()
                .filter(s -> apellido.equals(s.getApellido()))
                .findFirst();
    }

    private Optional<Sospechoso> getByEdad(Integer edad) {
        return sospechosos()
                .filter(s -> edad.equals(s.getEdad()))
                .findFirst();
    }

    private Optional<Sospechoso> getAsesino() {
        return sospechosos()
                .filter(s -> !s.isInocente())
                .findFirst();
    }

}
