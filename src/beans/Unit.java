/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.util.Objects;

/**
 * 
 * @author Jürgen Luttenberger, Philipp Nauschnegg
 */
public class Unit implements Serializable {

    private int unitID, Health, ad, ap, armor, magicres, range, movespeed, costs;
    private double attackspeed;
    private String displayname, typ;

    public Unit(int unitID, String displayname, int Health, int ad, int ap, int armor, int magicres, double attackspeed, int range, int movespeed, String typ, int costs) {
        this.unitID = unitID;
        this.Health = Health;
        this.ad = ad;
        this.ap = ap;
        this.armor = armor;
        this.magicres = magicres;
        this.range = range;
        this.movespeed = movespeed;
        this.costs = costs;
        this.attackspeed = attackspeed;
        this.displayname = displayname;
        this.typ = typ;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public int getHealth() {
        return Health;
    }

    public void setHealth(int Health) {
        this.Health = Health;
    }

    public int getAd() {
        return ad;
    }

    public void setAd(int ad) {
        this.ad = ad;
    }

    public int getAp() {
        return ap;
    }

    public void setAp(int ap) {
        this.ap = ap;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getMagicres() {
        return magicres;
    }

    public void setMagicres(int magicres) {
        this.magicres = magicres;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getMovespeed() {
        return movespeed;
    }

    public void setMovespeed(int movespeed) {
        this.movespeed = movespeed;
    }

    public int getCosts() {
        return costs;
    }

    public void setCosts(int costs) {
        this.costs = costs;
    }

    public double getAttackspeed() {
        return attackspeed;
    }

    public void setAttackspeed(double attackspeed) {
        this.attackspeed = attackspeed;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    @Override
    public String toString() {
        return String.format("ID: %02d | Name: %-20s | Health: %4d | Ad: %3d | Ap: %3d | Armor: %3d | MagicRes: %3d | AttackSpeed: %3.2f | Range: %3d | MoveSpeed: %3d | Typ: %20s | Costs: %3d", unitID, displayname, Health, ad, ap, armor, magicres, attackspeed, range, movespeed, typ, costs);
    }
    
    public String toShowString()
    {
        return String.format("%10s - H: %4d | AD: %3d | AP: %3d | A: %3d | M: %3d | AS: %3.2f | R: %3d | MS: %3d", displayname, Health, ad, ap, armor, magicres, attackspeed, range, movespeed);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Unit other = (Unit) obj;
        if (this.unitID != other.unitID) {
            return false;
        }
        if (!Objects.equals(this.displayname, other.displayname)) {
            return false;
        }
        return true;
    }
    
    

}
