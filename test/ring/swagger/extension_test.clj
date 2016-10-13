(ns ring.swagger.extension-test
  (:require [midje.sweet :refer :all]
            [ring.swagger.extension :as extension]))

(extension/defextension enabled (true? true))
(extension/defextension disabled (true? false))

(fact "defextension wrappers"
  (let [evals (atom [])]
    (fact "do not evaluate the body if the extension test is falsey"
      (disabled
        (swap! evals conj :a)
        (swap! evals conj :b)
        :c) => nil?
      @evals => empty?)
    (fact "do evaluate the body if the extension test is truthy"
      (enabled
        (swap! evals conj :d)
        (swap! evals conj :e)
        :f) => :f
      @evals => [:d :e]))
  (fact "undefined vars/classes are ignored in disabled extensions"
    (resolve 'a) => nil?
    (disabled (a)) =not=> (throws Exception)
    (resolve 'dummy.Class) => (throws ClassNotFoundException)
    (disabled (dummy.Class)) =not=> (throws Exception)))
