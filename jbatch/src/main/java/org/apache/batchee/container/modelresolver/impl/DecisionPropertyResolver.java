/*
 * Copyright 2012 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package org.apache.batchee.container.modelresolver.impl;

import org.apache.batchee.container.jsl.TransitionElement;
import org.apache.batchee.container.modelresolver.PropertyResolverFactory;
import org.apache.batchee.jaxb.Decision;

import java.util.Properties;

public class DecisionPropertyResolver extends AbstractPropertyResolver<Decision> {

    public DecisionPropertyResolver(boolean isPartitionStep) {
        super(isPartitionStep);
    }

    public Decision substituteProperties(final Decision decision, final Properties submittedProps, final Properties parentProps) {

        // resolve all the properties used in attributes and update the JAXB
        // model
        decision.setId(this.replaceAllProperties(decision.getId(), submittedProps, parentProps));
        decision.setRef(this.replaceAllProperties(decision.getRef(), submittedProps, parentProps));

        // Resolve all the properties defined for this decision
        Properties currentProps = parentProps;
        if (decision.getProperties() != null) {
            currentProps = this.resolveElementProperties(decision.getProperties().getPropertyList(), submittedProps, parentProps);
        }

        if (decision.getTransitionElements() != null) {
            for (final TransitionElement transitionElement : decision.getTransitionElements()) {
                PropertyResolverFactory.createTransitionElementPropertyResolver(this.isPartitionedStep).substituteProperties(transitionElement, submittedProps, currentProps);
            }
        }

        return decision;
    }

}
