# Protelis 
*Practical aggregate programming, hosted in Java*

**Table of Contents**

* [Why Protelis](#why)
 * [Further reading / references](#reading)
* [Developing with Protelis](#developing)
* [Contributing to Protelis](#contributing)
 * [Current build status of Protelis](#buildstatus)
* [History and Trivia](#history)

## <a name="why"></a>Why Protelis

Developing networked systems is really hard.
To make a good resilient system, you generally need to bring three types of expertise to bear all at once in the same code:

* Expertise regarding the actual purpose of the system
* Networking expertise, to manage the interactions between individual devices
* Distributed algorithms expertise, to ensure that the collective behavior is desirable and resilient to all sorts of failures and system changes

This "aggregate programming" problem, of obtaining resilient collective behavior from local interactions between machines, has been with us ever since people started networking computers.
In some specialized areas, such as cloud computing and GPU programming, there a simple and regular network architecture has already led to good aggregate programming methods (e.g., [MapReduce](https://en.wikipedia.org/wiki/MapReduce)).

The goal of the Protelis language is to make resilient networked systems just as easy to build for complex and heterogeneous networks as for single machines and cloud systems.
This accomplished by separating the different tasks and making some of the hard and subtle parts automatic and implicit.
A few of the key design decisions behind Protelis are:

* Protelis is a language because there are a lot of subtle and easy ways to break a distributed system.  Creating a language (rather than just a library) lets some of these be handled implicitly, so there is no opportunity to make mistakes.
* Protelis is hosted in and integrated with Java to let it be very lightweight take advantage of the large pre-existing ecosystem of Java infrastructure and libraries.
* Protelis looks as much like Java as practical in order to make it easier to learn and adopt.
* Protelis ensures safe and resilient composition because its core is *field calculus*, a theoretical model of aggregate programming much like [lambda calculus](https://en.wikipedia.org/wiki/Lambda_calculus) is for functional programming.

### <a name="reading"></a>Further reading / references:

* [Protelis: Practical Aggregate Programming](http://jakebeal.com/Publications/SAC2015-Protelis.pdf),
	Danilo Pianini, Mirko Viroli, Jacob Beal, ACM Symposium on Applied Computing 2015, April 2015.
	<br>*The first scientific paper presenting Protelis and example applications*
* **Distributed Recovery for Enterprise Services**
	Shane S. Clark, Jacob Beal, Partha Pal, 9th IEEE International Conference on Self-Adaptive and Self-Organizing Systems, to appear September 2015.
	<br>*Protelis applied to fast, low-impact automated recovery of enterprise systems*
* [A Calculus of Computational Fields](http://jakebeal.com/Publications/FOCLASA13-FieldCalculus.pdf), 
	Mirko Viroli, Ferruccio Damiani, and Jacob Beal, 12th International Workshop on Foundations of Coordination Languages and Self Adaptive Systems (FOCLASA'13), September 2013.
	<br>*Field calculus is the mathematical/theoretical foundation of Protelis*
* [Code Mobility Meets Self-Organisation: a Higher-order Calculus of Computational Fields](http://openmap.bbn.com/~jbeal/Publications/FORTE15-HigherOrderFieldCalculus.pdf), 
	Ferruccio Damiani, Mirko Viroli, Danilo Pianini, and Jacob Beal, Formal Techniques for Distributed Objects, Components, and Systems, pp. 113-128, June 2015.
	<br>*Higher-order field calculus lets Protelis have first-class functions*
* [Space-time Programming](http://rsta.royalsocietypublishing.org/content/373/2046/20140220), Jacob Beal and Mirko Viroli, Philosophical Transactions of the Royal Society A, Volume 373, Issue 2046, pages 20140220, June 2015.
	<br>*Larger picture of field calculus and general approach to aggregate programming, with a focus on spatially-distributed systems*
* [Organizing the Aggregate: Languages for Spatial Computing](http://arxiv.org/abs/1202.5509)
	Jacob Beal, Stefan Dulman, Kyle Usbeck, Mirko Viroli, and Nikolaus Correll, chapter in "Formal and Practical Aspects of Domain-Specific Languages: Recent Developments", ed. Marjan Mernik, IGI Global, December 2012.
	<br>*A survey of other aggregate programming approaches, with a focus on spatially-distributed networks*

## <a name="developing"></a>Developing with Protelis



## <a name="contributing"></a>Contributing to Protelis

Protelis is a free and open project that welcomes additional contributions.

If you want to help improve Protelis, just dive in, fork a copy of this code, and get started!
Development customs follow a standard GitHub workflow: get things working in your own branch, then request pull integration.
The file README.developer contains information about setting up development and contributing.

### <a name="buildstatus"></a>Current build status of Protelis:

[![Build Status](https://drone.io/github.com/Protelis/Protelis/status.png)](https://drone.io/github.com/Protelis/Protelis/latest)

* [Test details](https://drone.io/github.com/Protelis/Protelis/files/build/reports/tests/index.html)
* [FindBugs reporting](https://drone.io/github.com/Protelis/Protelis/files/build/reports/findbugs/main.html)
* [All build reports](https://drone.io/github.com/Protelis/Protelis/files/build/reports/reports.tar)

## <a name="history"></a>History and Trivia

Protelis emerged from the synthesis of several prior projects:

* [Proto](http://mitproto.net), an aggregate programming language created by [Jacob Beal](http://jakebeal.com) and [Jonathan Bachrach](http://www.eecs.berkeley.edu/~jrb/).
* [Field calculus](http://jakebeal.com/Publications/FOCLASA13-FieldCalculus.pdf), a distillation of aggregate programming models by [Mirko Viroli](http://www.apice.unibo.it/xwiki/bin/view/MirkoViroli/), [Ferruccio Damiani](http://www.di.unito.it/~damiani/), and [Jacob Beal](http://jakebeal.com)
* The [Alchemist Simulator](http://www.apice.unibo.it/xwiki/bin/view/Alchemist/) project, led by [Danilo Pianini](http://www.danilopianini.org/) and [Mirko Viroli](http://www.apice.unibo.it/xwiki/bin/view/MirkoViroli/).

The first version of Protelis was designed jointly during the summer of 2014 by Jacob Beal, Danilo Pianini, and Mirko Viroli, with the first implementation carried out primarily by Danilo Pianini.

The name "Protelis," a Latin word which trnanslates approximately as "regarding a team," was chosen to reflect both its nature as an aggregate language and its derivation from Proto.