//package io.github.math0898.junitTests;
//
//import io.github.math0898.rpgframework.damage.events.AdvancedDamageEvent;
//import io.github.math0898.rpgframework.damage.AdvancedDamageHandler;
//import io.github.math0898.rpgframework.damage.DamageResistance;
//import io.github.math0898.rpgframework.damage.DamageType;
//import org.bukkit.event.entity.EntityDamageEvent;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class DamageTests {
//
//    @Test
//    void damageCalculation() {
//        AdvancedDamageEvent advancedDamageEvent = new AdvancedDamageEvent(new EntityDamageEvent(new TestEntity(), EntityDamageEvent.DamageCause.FALL, 15.0));
//        advancedDamageEvent.getDamages().replace(DamageType.IMPACT, 15.0);
//
//        advancedDamageEvent.getResistances().replace(DamageType.IMPACT, DamageResistance.IMMUNITY);
//        assertEquals(0.00, AdvancedDamageHandler.damageCalculation(advancedDamageEvent));
//
//        advancedDamageEvent.getResistances().replace(DamageType.IMPACT, DamageResistance.RESISTANCE);
//        assertEquals(7.50, AdvancedDamageHandler.damageCalculation(advancedDamageEvent));
//
//        advancedDamageEvent.getResistances().replace(DamageType.IMPACT, DamageResistance.NORMAL);
//        assertEquals(15.00, AdvancedDamageHandler.damageCalculation(advancedDamageEvent));
//
//        advancedDamageEvent.getResistances().replace(DamageType.IMPACT, DamageResistance.SUSCEPTIBILITY);
//        assertEquals(22.50, AdvancedDamageHandler.damageCalculation(advancedDamageEvent));
//
//        advancedDamageEvent.getResistances().replace(DamageType.IMPACT, DamageResistance.VULNERABILITY);
//        assertEquals(30.00, AdvancedDamageHandler.damageCalculation(advancedDamageEvent));
//
//        advancedDamageEvent.setPhysicalResistance(0.3333);
//        assertEquals(20.00, AdvancedDamageHandler.damageCalculation(advancedDamageEvent), 0.5);
//
//        advancedDamageEvent.getResistances().replace(DamageType.IMPACT, DamageResistance.RESISTANCE);
//        assertEquals(5.00, AdvancedDamageHandler.damageCalculation(advancedDamageEvent), 0.5);
//    }
//}