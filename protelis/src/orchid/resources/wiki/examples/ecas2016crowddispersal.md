---
title: Crowd dispersal service
protelis_version: 0.1.0
paper_title: "Combining Self-Organisation and Autonomic Computing in CASs with Aggregate-MAPE"
paper_authors: "Mirko Viroli, Antonio Bucchiarone, Danilo Pianini, and Jacob Beal"
paper_venue: "the 2016 IEEE 1st International Workshops on Foundations and Applications of Self* Systems (FAS*W)"
doi: "10.1109/FAS-W.2016.49"
---

**Note:** This example uses Protelis version {{ protelis_version }}, while current version is {{ site.version }}.
As such, the provided code might not work with newer releases.

This example has been presented in the paper:

[**{{ paper_title }}** *by {{ paper_authors }}; published in {{ paper_venue }}*](https://doi.org/{{ doi }})
**Note:** This example uses an old Protelis version 8.0.3, while current version is {{ site.version }}.
As such, the provided code might not work as intended with newer releases.

# Crowd dispersal service

The source code and instructions on how to reproduce are [available online](https://bitbucket.org/danysk/experiment-2016-ecas/).

This example is an extension of {{ anchor('the example presented in "crowd danger estimation and warning"', 'Crowd danger estimation and warning') }}.
Besides detection and warning, it feature a simple dispersal service, advising users not to proceed on their destination,
but instead suggesting alternatives to prevent overcrowding. 

<div style="position:relative;padding-top:56.25%;">
<iframe style="position:absolute;top:0;left:0;width:100%;height:100%;" src="https://www.youtube.com/embed/606ObQwQuaE" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
</div>

This is an accelerated video showing a crowd dispersal application using the Alchemist simulator.
Each black dot is a handheld device, following real movement traces from the 2013 Vienna City Marathon.
Red dots are devices warned as being in dangerously crowded areas,
and yellow dots are devices warned that they are nearing a dangerously crowded area.
Blue dots represent people who follow the crowd dispersal application’s advice. 

