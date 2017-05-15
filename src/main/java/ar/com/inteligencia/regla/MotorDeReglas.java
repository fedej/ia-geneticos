package ar.com.inteligencia.regla;

import ar.com.inteligencia.Sospechoso;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jenetics.BitGene;
import org.jenetics.Genotype;

@Value
@RequiredArgsConstructor
public class MotorDeReglas {

    private final Reglas reglas;
    public static final int FAIL_SCORE = 1;
    public static final int PUNISH_SCORE = 1;

    public MotorDeReglas(Genotype<BitGene> gt) {
        reglas = new Reglas(gt);
    }

    public double evaluar() {
        double base = 0;

        base += reglas._1_felixMasJovenQueAbad();
        base += reglas._2_viudoY50SonInocentes();
        base += reglas._3_asesinoYDivorciadoAcusanAGustavo();
        base += reglas._4_casadoQuiereASuMujer();
        base += reglas._5_primos();
        base += reglas._6_solteroMasJovenQueFelix();
        base += reglas._7_jesusDomino();
        base += reglas._8_abadY54Inocentes();
        base += reglas._9_gustavoMayorQueViudo();
        base += reglas._10_villarCampeonBillar();
        base += reglas._11_cerveraBisiesto();
        base += reglas._12_ramiroMasJovenQueLamata();

        return base;
    }

    public boolean isValid() {
        return
                reglas._regla1Solo(Sospechoso::getNombre)
                        && reglas._regla1Solo(Sospechoso::getApellido)
                        && reglas._regla1Solo(Sospechoso::getEdad)
                        && reglas._regla1Solo(Sospechoso::getEstadoCivil)
                        && reglas._regla1Solo(Sospechoso::isCampeonDeBillar)
                        && reglas._regla1Solo(Sospechoso::isAcusoAGustavo)
                        && reglas._regla1Solo(Sospechoso::isAcusoAGustavo2)
                        && reglas._regla1Solo(Sospechoso::isAficionadoAlDomino)
                        && reglas._regla1Solo(Sospechoso::isNacioEnAnioBisiesto)
                        && reglas._regla1Solo(Sospechoso::isPrimosEnLondres)
                        && reglas._regla1Solo(Sospechoso::isPrimosEnParis)
                        && reglas._regla1Solo(Sospechoso::isQuiereASuMujer)
                        && reglas._13_unSoloAsesino();
    }


}
