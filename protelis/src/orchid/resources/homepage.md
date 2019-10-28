---
components:
#  - type: 'readme'
---

<link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
<link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
<link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png">
<link rel="manifest" href="/site.webmanifest">
<link rel="mask-icon" href="/safari-pinned-tab.svg" color="#5bbad5">
<meta name="msapplication-TileColor" content="#00aba9">
<meta name="theme-color" content="#ffffff">

# ![Protelis Logo]({{ 'assets/media/images/logo.png' | asset | scale(0.08) }}) Why Protelis? What is it?

Protelis is a programming language designed for people who want to get resilient collective behavior from a complex network of heterogeneous machines.
Protelis leverages *aggregate programming* for applications like the Internet of Things or flocking robots.
It lets you think about what you want the collection of machines to accomplish, rather than what you need each individual machine to do.

The goal of the Protelis language is to make it easier to build a resilient and well-behaved networked system out of an assortment of different potentially mobile devices.
Protelis is designed for the paradigm of "aggregate programming",
a way of thinking about and decomposing problems that can be solved with a network of distributed sensors and computers.
Aggregate programming tries to produce reliable and robust collective behavior from uncoordinated local interactions between machines.
That's hard to do, but Protelis helps!

![Protelis Logo]({{ 'assets/media/images/overview.png' | asset }})

# What is it **not**?

## **Not** an agent-based modeling framework

Although agent-based modeling (ABM) and aggregate programming both concern phenomena that occur when many individuals interact, they approach these phenomena in different ways.
In ABM, you usually specify an individual’s behavior and interaction rules, and then simulate a collection of individuals to see what large-scale phenomena occur.
In aggregate programming, you specify the desired collective action, which is then transformed into a distributed implementation for individual machines.

## **Not** a simulation platform

Protelis is a programming language which can be used *within* simulation frameworks, but that per-se ships no simulation platform.
One of the nice things you get out of it is that, if the simulation implementation respects the reference,
the code written for a simulation can be reused as-is inside a deployed system.

