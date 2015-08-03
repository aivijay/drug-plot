(defproject data-vis "0.1.0-SNAPSHOT"
  :description "Data visualization tool to view historic drug and impact in scatter plots"
  :url ""
  :license {:name "Apache License 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [incanter "1.5.6"]
                 [incanter/incanter-core "1.5.6"]
                 [incanter/incanter-io "1.5.6"]
                 [incanter/incanter-charts "1.5.6"]
                 [incanter/incanter-processing "1.3.0"]
                 [org.clojure/data.csv "0.1.2"]]
  :jvm-opts ["-Xmx3g" "-server"])
