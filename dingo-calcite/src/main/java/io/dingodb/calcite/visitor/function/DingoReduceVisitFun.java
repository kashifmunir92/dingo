/*
 * Copyright 2021 DataCanvas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.dingodb.calcite.visitor.function;

import io.dingodb.calcite.rel.DingoReduce;
import io.dingodb.calcite.type.converter.DefinitionMapper;
import io.dingodb.calcite.visitor.DingoJobVisitor;
import io.dingodb.common.Location;
import io.dingodb.exec.base.IdGenerator;
import io.dingodb.exec.base.Job;
import io.dingodb.exec.base.Operator;
import io.dingodb.exec.base.Output;
import io.dingodb.exec.base.Task;
import io.dingodb.exec.operator.ReduceOperator;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

import static io.dingodb.calcite.rel.DingoRel.dingo;
import static io.dingodb.common.util.Utils.sole;

public class DingoReduceVisitFun {
    public static Collection<Output> visit(
        Job job, IdGenerator idGenerator, Location currentLocation, DingoJobVisitor visitor, @NonNull DingoReduce rel
    ) {
        Collection<Output> inputs = dingo(rel.getInput()).accept(visitor);
        Operator operator;
        operator = new ReduceOperator(AggFactory.getAggKeys(rel.getGroupSet()),
            AggFactory.getAggList(rel.getAggregateCallList(),
                DefinitionMapper.mapToDingoType(rel.getOriginalInputType())
            )
        );
        operator.setId(idGenerator.get());
        Output input = sole(inputs);
        Task task = input.getTask();
        task.putOperator(operator);
        input.setLink(operator.getInput(0));
        return operator.getOutputs();
    }
}
