# μnit

A small library for units.

Design goals:

- Units are implemented independent of SI.
- ... though an SI unit system is provided.
- Units are represented mostly as data.

## Non-goal: digging underneath base units

We don't aim to make it more abstract / more general than base units.

If base units were all defined as symbolic values, where arithmetic on symbolic
values just reduced to the expression,

    (* 4 'm)
    ;; => (* 4 m)

then we'd have units. Sums and products would work out of the box for symbolic
calculations.

    (+ 'm 'm)
    ;; => (* 2 m)
