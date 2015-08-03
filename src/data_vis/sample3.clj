(ns data-vis.sample3
  (:use [incanter core processing]))

(defn rotate-box [sktch rad]
  (doto sktch
    push-matrix
    (fill 196 196 (mod rad 256))
    (rect-mode CENTER)
    (translate 100 100)
    (rotate (radians rad))
    (rect 0 0 100 100)
    (rect-mode CORNER)
    pop-matrix))

(defn draw-header [sktch rad]
  (doto sktch
    (background 255)
    (fill 196 (mod rad 256) 0)
    (create-font "Serif" 16)
    (text-align LEFT)
    (text "This is an example." 50 20)
    (rect 0 50 50 50)))

(defn draw-footer [sktch]
  (doto sktch
    (fill 255 196 0 200)
    (ellipse 100 150 50 50)))

(defn rotate-example []
  (let [rad (ref 0)
	sktch (sketch
	       (setup []
		      (doto this
			(size 200 200)
			(background 255)
			smooth
			no-stroke))
	       (draw []
		     (doto this
		       (draw-header @rad)
		       (rotate-box @rad)
		       draw-footer
		       (save "proc_example1.png"))
		     (dosync (alter rad inc))))]
    (view sktch :size [250 250] :title "rotate example")))

(rotate-example)
