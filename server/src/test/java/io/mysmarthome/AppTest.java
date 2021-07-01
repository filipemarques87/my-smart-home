package io.mysmarthome;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }
}


/*

devices:
  - plataform: mqtt
    type: sensor
    topic: /home/room/temperature
    name: Temperatura Sala
    units: C
    notifications:
    - message: {{ Temperatura demasiada elevada $v }}
      trigger: {{ $v > 30 }}
    - message: {{ Temperatura demasiada baixa $v }}
      trigger: {{ $v < 5 }}
    read_every: 300 | * * * * * 1

  - plataform: mqtt
    type: switch
    topic: /home/room/light
    name: Luz Sala


 */