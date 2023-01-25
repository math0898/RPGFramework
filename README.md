# RPGFramework
The main framework plugin used for all of Math0898/Sugaku's RPG  plugins.

## Overhauled Damage System 
We've completely overhauled the vanilla Minecraft damage system in favor of one which gives more room for upward growth 
and nuance. The new damage system contains 3 physical types along with 6 elemental magic types and 3 higher magic types. 
The order damage is calculated in goes as follows:

0. The damage values are established.
1. Damage reduction provided by armor is applied.
2. Resistance to damage types is applied.



Let's use an attack of 60.0 Lighting Damage and 30.0 Slashing Damage as an example. For the sake of simplicity lets say 
our victim has the following: 

>  0.33 Magic Resistance <br>
>  0.50 Physical Resistance <br>
>  A Slash Damage Vulnerability <br>
>  A Lighting Damage Resistance <br>

- __Step 0)__ has already been taken care of since we declared our damage values. <br>
- __Step 1-A)__ We applied our damage reduction provided by armor. We start with the 30.0 slashing damage. After applying the 
physical damage resistance of 0.50 the new damage is 15.0 slashing. <br>
- __Step 1-B)__ Next we do the same with the 60.0 Lighting Damage and 0.33 magic resistance to obtain a new value of 40.0 
Lighting Damage. <br>
- __Step 2-A)__ Our character has a slashing vulnerability which means damage from that source is double. Multiply by 2 and 
the final physical damage is 30.0 slashing. <br>
- __Step 2-B)__ Luckily our character has a Lighting resistance and so we half the damage taken by lighting which means the 
final magic damage is 20.0 lighting. <br>

This is then scaled down and applied to the character as 5.0 damage.


Actual damage is more complicated as all the damage types may be present along with more resistance levels. 

## Parties

RPGFramework adds its own party system into the game of Minecraft. Parties are created when a player first decides to invite 
another player using `/party invite <username>` and are deleted after the last player leaves or logs off. The maximum capacity
of a party is 6 unless the leader has the `rpg.parties.maxbypass` permission. Party members have access to their own private 
chat by using `/party chat`. Leaders have the ability to summon their party members using `/party summon`. 

Planned features for parties include:
- __Party wide buffing with classes.__
- __Name color dependent on gear score.__
- __Prefix dependent on primary class.__

## Permissions

- __rpg__
  - __rpg.parties__
    - __rpg.parties.maxbypass__ - _Allows party leaders with this permission to bypass the maximum party size of 6._