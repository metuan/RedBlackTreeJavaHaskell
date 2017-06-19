import java.util.ArrayList;

/*
 * A sample implementation of persistent red-black trees labelled with
 * integers.
 */
public abstract class RedBlackTree {
   /*
    * An empty tree.
    */
   public final static RedBlackTree empty = new Leaf();
   /**
    * Inserts an element to the tree.
    * Duplicates are replaced with new values.
    * @param e An element to be inserted.
    * @return A new tree with an element inserted.
    */
   public RedBlackTree insert(int e) {
      RB t = this.ins(e);
      return new Black(t.l(), t.x(), t.r());
   }
   /*
    * Creates an array containing in nondecreasing order all elements
    * stored in a tree.
    * @return An array of elements stored in a tree.
    */
   public int[] flatten() {
      ArrayList<Integer> c = new ArrayList<Integer>();
      this.flatten(c);
      int[] result = new int[c.size()];
      int i = 0;
      for (int e : c) {
         result[i++] = e;
      }
      return result;
   }

   private RedBlackTree() {}
   protected abstract void flatten(ArrayList<Integer> c);
   protected abstract RB ins(int e);

   private interface RBL {
      RB ins(int e, int x, RBL r);
      RB ins(int e, RBL l, int x);
      void flatten(ArrayList<Integer> c);
   }
   private interface RB extends RBL {
      RBL l();
      RBL r();
      int x();
   }
   private interface BL extends RBL {
      RB ins(int e);
   }

   private static class Leaf extends RedBlackTree implements BL {
      public Red ins(int e) {
         return new Red(this, e, this);
      }
      public Black ins(int e, int x, RBL r) {
         return new Black(new Red(this, e, this), x, r);
      }
      public Black ins(int e, RBL l, int x) {
         return new Black(l, x, new Red(this, e, this));
      }
      public void flatten(ArrayList<Integer> c) {}
   }

   private static class Black extends RedBlackTree implements BL, RB {
      private final RBL l, r;
      private final int x;
      public RBL l() { return this.l; }
      public RBL r() { return this.r; }
      public int x() { return this.x; }
      public Black(RBL l, int x, RBL r) {
         this.l = l;
         this.x = x;
         this.r = r;
      }
      public RB ins(int e) {
         if (e <= this.x) {
            if (e >= this.x) {
               return new Black(this.l, e, this.r);
            } else {
               return this.l.ins(e, this.x, this.r);
            }
         } else {
            return this.r.ins(e, this.l, this.x);
         }
      }
      public RB ins(int e, RBL l, int x) {
         return new Black(l, x, this.ins(e));
      }
      public RB ins(int e, int x, RBL r) {
         return new Black(this.ins(e), x, r);
      }
      public void flatten(ArrayList<Integer> c) {
         this.l.flatten(c);
         c.add(x);
         this.r.flatten(c);
      }
   }

   private static class Red implements RB {
      private final BL l, r;
      private final int x;
      public BL l() { return this.l; }
      public BL r() { return this.r; }
      public int x() { return this.x; }
      public Red(BL l, int x, BL r) {
         this.l = l;
         this.x = x;
         this.r = r;
      }
      public RB ins(int e, int x, RBL r) {
         if (e <= this.x) {
            if (e >= this.x) {
               return new Black(new Red(this.l, e, this.r), x, r);
            } else {
               RB l1 = this.l.ins(e);
               if (l1 instanceof Red) {
                  return new Red(new Black(l1.l(), l1.x(), l1.r()), this.x,
                                 new Black(this.r, x, r));
               } else {
                  return new Black(new Red(new Black(l1.l(), l1.x(), l1.r()),
                                           this.x, this.r), x, r);
               }
            }
         } else {
            RB r1 = this.l.ins(e);
            if (r1 instanceof Red) {
               return new Red(new Black(this.l, this.x, r1.l()), r1.x(),
                              new Black(r1.r(), x, r));

            } else {
               return new Black(new Red(this.l, this.x,
                                    new Black(r1.l(), r1.x(), r1.r())), x, r);
            }
         }
      }
      public RB ins(int e, RBL l, int x) {
         if (e <= this.x) {
            if (e >= this.x) {
               return new Black(l, x, new Red(this.l, x, this.r));
            } else {
               RB l1 = this.l.ins(e);
               if (l1 instanceof Red) {
                  return new Red(new Black(l, x, l1.l()), l1.x(),
                                 new Black(l1.r(), this.x, this.r));
               } else {
                  return new Black(l, x, new Red(new Black(l1.l(),
                                          l1.x(), l1.r()), this.x, this.r));
               }
            }
         } else {
            RB r1 = this.r.ins(e);
            if (r1 instanceof Red) {
               return new Red(new Black(l, x, this.l), this.x,
                              new Black(r1.l(), r1.x(), r1.r()));

            } else {
               return new Black(l, x, new Red(this.l, this.x,
                                          new Black(r1.l(), r1.x(), r1.r())));
            }
         }
      }
      public void flatten(ArrayList<Integer> c) {
         this.l.flatten(c);
         c.add(x);
         this.r.flatten(c);
      }
   }
}