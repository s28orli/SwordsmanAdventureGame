package entity;

/**
 * EntityFacing: Represents the player's texture facing.
 *
 * @author Samuil Orlioglu
 * @version 4/27/2020
 */


public enum EntityFacing {
    Back(0), Left(1), Front(2), Right(3);
    public final int value;

    EntityFacing(int value){
        this.value = value;
    }


}
