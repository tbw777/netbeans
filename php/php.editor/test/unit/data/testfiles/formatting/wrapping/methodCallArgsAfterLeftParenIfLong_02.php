<?php
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//test(
//$param1, $param2, $param3);
test(
$param1, $param2, $param3);

//test($param1,
//        $param2, $param3);
test($param1,
        $param2, $param3);

//test($param1,$param2, $param3
//        );
test($param1,$param2, $param3
        );

//test(
//$param1,
//$param2,
//$param3
//);
test(
$param1,
$param2,
$param3
);

//test(
//        param1: $param1, param2: $param2, param3: $param3);
test(
        param1: $param1, param2: $param2, param3: $param3);

//test(param1: $param1,
//        param2: $param2, param3: $param3);
test(param1: $param1,
        param2: $param2, param3: $param3);

//test(param1: $param1,param2: $param2, param3: $param3
//        );
test(param1: $param1,param2: $param2, param3: $param3
        );

//test(
//param1: $param1,
//param2: $param2,
//param3: $param3
//        );
test(
param1: $param1,
param2: $param2,
param3: $param3
        );

// array
//test(
//[
//1,
//2,
//3
//]);
test(
[
1,
2,
3
]);

//test([
//1,
//2,
//3
//]
//        );
test([
1,
2,
3
]
        );

//test(
//        [
//1,
//2,
//3
//]
//        );
test(
        [
1,
2,
3
]
        );

//test(
//test: [
//1,
//2,
//3
//]);
test(
test: [
1,
2,
3
]);

//test(test: [
//1,
//2,
//3
//]
//);
test(test: [
1,
2,
3
]
);

//test(
//test: [
//1,
//2,
//3
//]
//);
test(
test: [
1,
2,
3
]
);

// lambda function
//test(
//function (): int {
//return 1;
//});
test(
function (): int {
return 1;
});

//test(function (): int {
//return 1;
//}
//);
test(function (): int {
return 1;
}
);

//test(
//        function (): int {
//return 1;
//}
//);
test(
        function (): int {
return 1;
}
);

//test(
//test   :    function (): int {
//return 1;
//});
test(
test   :    function (): int {
return 1;
});

//test(test   :    function (): int {
//return 1;
//}
//);
test(test   :    function (): int {
return 1;
}
);

//test(
//test   :    function (): int {
//return 1;
//}
//);
test(
test   :    function (): int {
return 1;
}
);

// match expression
//test(
//"test", match (true) {
//    true => "true",
//    false => 'false',
//});
test(
"test", match (true) {
    true => "true",
    false => 'false',
});

//test("test", match (true) {
//    true => "true",
//    false => 'false',
//}
//);
test("test", match (true) {
    true => "true",
    false => 'false',
}
);

//test(
//        "test", match (true) {
//    true => "true",
//    false => 'false',
//}
//);
test(
        "test", match (true) {
    true => "true",
    false => 'false',
}
);

//test(
//        "test",
//        match (true) {
//    true => "true",
//    false => 'false',
//}
//);
test(
        "test",
        match (true) {
    true => "true",
    false => 'false',
}
);

//test(
//        test1: "test", test2: match (true) {
//    true => "true",
//    false => 'false',
//});
test(
        test1: "test", test2: match (true) {
    true => "true",
    false => 'false',
});

//test(test1: "test", test2: match (true) {
//    true => "true",
//    false => 'false',
//}
//);
test(test1: "test", test2: match (true) {
    true => "true",
    false => 'false',
}
);

//test(
//        test1: "test", test2: match (true) {
//    true => "true",
//    false => 'false',
//}
//);
test(
        test1: "test", test2: match (true) {
    true => "true",
    false => 'false',
}
);

//test(
//        test1: "test",
//test2: match (true) {
//    true => "true",
//    false => 'false',
//}
//);
test(
        test1: "test",
test2: match (true) {
    true => "true",
    false => 'false',
}
);

// anonymous class
//test(
//        new class {
//public function foo() {
//}
//});
test(
        new class {
public function foo() {
}
});

//test(new class {
//public function foo() {
//}
//}
//);
test(new class {
public function foo() {
}
}
);

//test(
//new class {
//public function foo() {
//}
//}
//);
test(
new class {
public function foo() {
}
}
);

//test(
//        test:new class {
//public function foo() {
//}
//});
test(
        test:new class {
public function foo() {
}
});

//test(test:new class {
//public function foo() {
//}
//}
//);
test(test:new class {
public function foo() {
}
}
);

//test(
//test:new class {
//public function foo() {
//}
//}
//);
test(
test:new class {
public function foo() {
}
}
);

//$test->test(
//        $param1, $param2, $param3);
$test->test(
        $param1, $param2, $param3);

//$test->test($param1, $param2, $param3
//        );
$test->test($param1, $param2, $param3
        );

//$test->test(
//        $param1, $param2, $param3
//        );
$test->test(
        $param1, $param2, $param3
        );

//$test->test(
//        $param1,
//        $param2,
//        $param3
//        );
$test->test(
        $param1,
        $param2,
        $param3
        );

//$test->test(
//        param1: $param1, param2: $param2, param3: $param3);
$test->test(
        param1: $param1, param2: $param2, param3: $param3);

//$test->test(param1: $param1, param2: $param2, param3: $param3
//        );
$test->test(param1: $param1, param2: $param2, param3: $param3
        );

//$test->test(
//        param1: $param1, param2: $param2, param3: $param3
//        );
$test->test(
        param1: $param1, param2: $param2, param3: $param3
        );

//$test->test(
//        param1: $param1,
//        param2: $param2,
//        param3: $param3
//        );
$test->test(
        param1: $param1,
        param2: $param2,
        param3: $param3
        );

// array
//$test->test(
//        [
//1,
//2,
//3
//]);
$test->test(
        [
1,
2,
3
]);

//$test->test([
//1,
//2,
//3
//]
//        );
$test->test([
1,
2,
3
]
        );

//$test->test(
//        [
//1,
//2,
//3
//]
//        );
$test->test(
        [
1,
2,
3
]
        );

//$test->test(
//        test: [
//1,
//2,
//3
//]);
$test->test(
        test: [
1,
2,
3
]);

//$test->test(test: [
//1,
//2,
//3
//]
//        );
$test->test(test: [
1,
2,
3
]
        );

//$test->test(
//        test: [
//1,
//2,
//3
//]
//        );
$test->test(
        test: [
1,
2,
3
]
        );

// lambda function
//$test->test(
//        function (): int {
//return 1;
//});
$test->test(
        function (): int {
return 1;
});

//$test->test(function (): int {
//return 1;
//}
//);
$test->test(function (): int {
return 1;
}
);

//$test->test(
//        function (): int {
//return 1;
//}
//);
$test->test(
        function (): int {
return 1;
}
);

//$test->test(
//test   :    function (): int {
//return 1;
//});
$test->test(
test   :    function (): int {
return 1;
});

//$test->test(test   :    function (): int {
//return 1;
//}
//);
$test->test(test   :    function (): int {
return 1;
}
);

//$test->test(
//        test   :    function (): int {
//return 1;
//}
//);
$test->test(
        test   :    function (): int {
return 1;
}
);

// match expression
//$test->test(
//        "test", match (true) {
//    true => "true",
//    false => 'false',
//});
$test->test(
        "test", match (true) {
    true => "true",
    false => 'false',
});

//$test->test("test", match (true) {
//    true => "true",
//    false => 'false',
//}
//);
$test->test("test", match (true) {
    true => "true",
    false => 'false',
}
);

//$test->test(
//        "test", match (true) {
//    true => "true",
//    false => 'false',
//}
//);
$test->test(
        "test", match (true) {
    true => "true",
    false => 'false',
}
);

//$test->test(
//        "test",
//        match (true) {
//    true => "true",
//    false => 'false',
//}
//);
$test->test(
        "test",
        match (true) {
    true => "true",
    false => 'false',
}
);

//$test->test(
//        test1: "test", test2: match (true) {
//    true => "true",
//    false => 'false',
//});
//
$test->test(
        test1: "test", test2: match (true) {
    true => "true",
    false => 'false',
});

//$test->test(test1: "test", test2: match (true) {
//    true => "true",
//    false => 'false',
//}
//);
$test->test(test1: "test", test2: match (true) {
    true => "true",
    false => 'false',
}
);

//$test->test(
//        test1: "test", test2: match (true) {
//    true => "true",
//    false => 'false',
//}
//);
$test->test(
        test1: "test", test2: match (true) {
    true => "true",
    false => 'false',
}
);

//$test->test(
//        test1: "test",
//        test2: match (true) {
//    true => "true",
//    false => 'false',
//}
//);
$test->test(
        test1: "test",
        test2: match (true) {
    true => "true",
    false => 'false',
}
);

// anonymous class
//$test->test(
//        new class {
//public function foo() {
//}
//});
$test->test(
        new class {
public function foo() {
}
});

//$test->test(new class {
//public function foo() {
//}
//}
//);
$test->test(new class {
public function foo() {
}
}
);

//$test->test(
//        new class {
//public function foo() {
//}
//}
//);
$test->test(
        new class {
public function foo() {
}
}
);

//$test->test(
//        test:new class {
//public function foo() {
//}
//});
$test->test(
        test:new class {
public function foo() {
}
});

//$test->test(test:new class {
//public function foo() {
//}
//}
//);
$test->test(test:new class {
public function foo() {
}
}
);

//$test->test(
//        test:new class {
//public function foo() {
//}
//}
//);
$test->test(
        test:new class {
public function foo() {
}
}
);
