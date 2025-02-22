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

package io.dingodb.exec.fun.string;

import io.dingodb.expr.runtime.RtExpr;
import io.dingodb.expr.runtime.op.RtStringFun;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LeftFun extends RtStringFun {
    public static final String NAME = "left";
    private static final long serialVersionUID = 5242457055774200528L;

    /**
     * Create an DingoStringLeftOp. DingoStringLeftOp extract left sub string.
     *
     * @param paras the parameters of the op
     */
    public LeftFun(RtExpr[] paras) {
        super(paras);
    }

    public static @NonNull String leftString(final String str, int cnt) {
        if (str == null || str.equals("") || cnt < 0) {
            return "";
        }

        return cnt > str.length() ? str : str.substring(0, cnt);
    }

    @Override
    protected Object fun(Object @NonNull [] values) {
        String str = String.valueOf(values[0]);
        int cnt = new BigDecimal(String.valueOf(values[1]))
            .setScale(0, RoundingMode.HALF_UP).intValue();

        return leftString(str, cnt);
    }

}
