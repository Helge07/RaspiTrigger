# RasPiTrigger 
How to take Water drop photos with the Raspberry Pi: 
A hardware + software solution based on the Raspberry PI

## Table of contents
- [Introduction](#Introduction)
- [Requirements](#Requirements)
- [Compilation_and_Installation](#Compilation_and_Installation)
- [Usage](#usage)
- [License](#license)

## Introduction
When photographing water drop sculptures, exact timing is very important:

- the size and release time of the drops must be precisely controlled by solenoid valves,
- the flashes must triggered within milliseconds,
- the flash duration must be < 100 microseconds to freeze the movement

## Requirements
Until now solutions for exact, reproducible timing are based on
- Devices whose parameters are adjusted via hardware controllers or
- Hardware controllers and Arduino or Raspberry Pi based DYI solutions that receive control data from a PC

The RasPiTrigger differs from the above solutions in that all functionalities are implemented on a Raspberry Pi, namely:
- the setting of parameters via a graphical user interface and
- the timing of all processes (camera shutter, flash units and solenoid valves).
- The connection to the connected devices is established by a junction box, which electrically separates them from the GPIO ports of the Raspberry Pi.
 
## Compilation_and_Installation
To compile the `RasPiTrigger` program, follow these steps:
- get the repository from https://github.com/Helge07/RaspiTrigger
- open the workspace  `...\eclipse-workspace` with Eclipse 
- run `RasPiTrigger`
- an already compiled `RasPiTrigger.jar` can be found in the directory  ...\eclipse-workspace\RasPiTrigger\Export.

- `RasPiTrigger` is designded to run on a Raspberry Pi, but to play with the user interface it can directly be called under Windows.

## Usage   
Detailed instructions can be found in the directiory /docs:
- a general description of the `RasPiTrigger` system
- instructions for the configuration of the Raspberry Pi
- instructions for installing the `RasPiTrigger` on the Raspberry Pi
- instructions for building the junction box.

## RasPiTrigger on GitHub Pages
- https://helge07.github.io/RaspiTrigger/

## License
EosMonitor is published under the GPL-3.0 license. See the LICENSE file for more information. 




