{:shared {:clean-targets ["out" :target-path]}


 :dev [:shared
       {:resources-paths ["dev-resources"]
        :source-paths ["dev-resources/tools/http" "dev-resources/tools/repl"]
        :dependencies [[ring "1.2.1"]
                       [compojure "1.1.6"]
                       [enlive "1.1.5"]]
        :plugins [[weasel "0.3.0"]]
        :cljsbuild
        {:builds {:sanic
                  {:source-paths ["dev-resources/tools/repl"]
                   :compiler
                   {:optimizations :whitespace
                    :pretty-print true}}}}

        :injections [(require '[ring.server :as http :refer [run]]
                              'weasel.repl.websocket
                              'cemrick.piggieback)
                     (defn browser-repl-env []
                       (reset! weasel.repl.websocket/browser-repl-env
                               (weasel.repl.websocket/repl-env
                                :ip "0.0.0.0" :port 9001)
                               ))
                     (defn browser-repl []
                       (cemrick.piggieback/cljs-repl
                         :repl-env (browser-repl-env)))]}]}
