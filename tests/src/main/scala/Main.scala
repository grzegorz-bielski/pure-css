package pureframes.css.tests

import pureframes.css.*

@main
def run = 
    println(styles)

inline val color = "tomato"
val styles = css"""
    color: $color;
"""