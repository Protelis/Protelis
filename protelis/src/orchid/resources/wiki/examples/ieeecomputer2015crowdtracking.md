---
title: "Crowd danger estimation and warning"
protelis_version: 0.8.3
paper_title: "Aggregate Programming for the Internet of Things"
paper_authors: "Jacob Beal, Danilo Pianini, and Mirko Viroli"
paper_venue: "IEEE Computer"
doi: "10.1109/MC.2015.261"
---

**Note:** This example uses Protelis version {{ protelis_version }}, while current version is {{ site.version }}.
As such, the provided code might not work with newer releases.

This example has been presented in the paper:

[**{{ paper_title }}** *by {{ paper_authors }}; published in {{ paper_venue }}*](https://doi.org/{{ doi }})

Any time an event or attraction draws a crowd,
individuals may continue to move into the most crowded areas and inadvertently obstruct egress,
creating dangerously dense-packed locations vulnerable to stampedes.
In addition to the motivation of avoiding danger,
people may also want to avoid highly congested spots because the experience is unpleasant.
Distributed sensing and computing can help solve this problem.

Using Protelis and the principles of aggregate programming,
it is straightforward to write a scalable and robust distributed application to detect severe crowding and help individuals navigate around highly congested areas.
We illustrate the use of several core Protelis operators and built-in functions.

Using some of Protelis’s special operators and syntax, a small number of functions can be defined and applied to variables.
A crowd safety service can use information about the number of nearby devices to issue warnings about areas with dangerous crowding levels.

The function `dangerousDensity` flags whether a location has a high or low danger.
The function determines danger by estimating the local density of people,
using the values of p (proportion of people with a participating device running this app),
r (the radius of interest),
and checking large groups of people (>300) against a density danger cut-off value of 2.17 people per square meter.

```javascript
def dangerousDensity(p, r) {
  let mr =  managementRegions(r*2, () -> { nbrRange });
  let danger =  average(mr, densityEst(p, r)) > 2.17 && summarize(mr, sum, 1 / p, 0 ) > 300;
  if(danger) { high } else { low }
}
```

The function `crowdTracking` checks for a dangerous density in crowded areas, where 1.08 people per square meter is used as the cut-off to define crowded.

```javascript
def crowdTracking(p, r, t) {
  let crowdRgn =  recentTrue(densityEst(p, r) > 1.08, t);
  if(crowdRgn) { densityEst(p, r) } else { none }
}
```

The function `crowdWarning` alerts individuals who are near dangerously crowded spots.

```javascript
def crowdWarning(p, r, warn, t) {
  distanceTo(crowdTracking(p, r, t) == high) < warn
}
```

![Vienna Marathon]({{ 'assets/media/images/examples/ieeecomputer2015crowdtracking/marathon.png' | asset }})

In this simulation of the 2013 Vienna Marathon, dots represent devices running the crowd safety service.
Red dots are devices in locations with potentially dangerous crowd density and yellow dots are devices providing crowd density warnings.

![Crowd Management]({{ 'assets/media/images/examples/ieeecomputer2015crowdtracking/crowd-management.jpg' | asset }})

In this simulation of public events near the Boston waterfront, dots represent devices running the crowd safety service.
Red dots are devices in locations with potentially dangerous crowd density and yellow dots are devices providing crowd density warnings.
