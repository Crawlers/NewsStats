/*
 *  Gate.java
 *
 * Copyright (c) 2000-2012, The University of Sheffield.
 *
 * This file is part of GATE (see http://gate.ac.uk/), and is free
 * software, licenced under the GNU Library General Public License,
 * Version 3, 29 June 2007.
 *
 * A copy of this licence is included in the distribution in the file
 * licence.html, and is also available at http://gate.ac.uk/gate/licence.html.
 *
 *  tharindu, 11/7/2014
 *
 * For details on the configuration options, see the user guide:
 * http://gate.ac.uk/cgi-bin/userguide/sec:creole-model:config
 */

package com.cse10.extractor;

import gate.LanguageResource;
import gate.creole.AbstractLanguageResource;
import gate.creole.metadata.CreoleResource;


/** 
 * This class is the implementation of the resource GATE.
 */
@CreoleResource(name = "gate",
        comment = "Add a descriptive comment about this resource")
public class Gate  extends AbstractLanguageResource
  implements LanguageResource {


} // class Gate
