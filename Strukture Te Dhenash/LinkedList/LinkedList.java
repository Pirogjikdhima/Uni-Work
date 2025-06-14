package LinkedList;

@SuppressWarnings("ALL")
public class LinkedList<AnyType extends Comparable<AnyType>> {
    private ListNode<AnyType> header;

    public LinkedList() {
        header = new ListNode<AnyType>(null);
    }

    public boolean isEmpty() {
        return header.next == null;
    }

    public void makeEmpty() {
        header.next = null;
    }

    public LinkedListIterator<AnyType> zeroth() {
        return new LinkedListIterator<AnyType>(header);
    }

    public LinkedListIterator<AnyType> first() {
        return new LinkedListIterator<AnyType>(header.next);
    }

    public void insert(AnyType x, LinkedListIterator<AnyType> p) {
        if (p != null && p.current != null) {
            p.current.next = new ListNode<AnyType>(x, p.current.next);
        }
    }

    public LinkedListIterator<AnyType> find(AnyType x) {
        ListNode<AnyType> node = header.next;
        while (node != null && !node.element.equals(x)) {
            node = node.next;
        }
        return new LinkedListIterator<AnyType>(node);
    }

    public LinkedListIterator<AnyType> findPrevious(AnyType x) {
        ListNode<AnyType> itr = header;
        while (itr.next != null && !itr.next.element.equals(x)) {
            itr = itr.next;
        }
        return new LinkedListIterator<AnyType>(itr);
    }

    public void remove(AnyType x) {
        LinkedListIterator<AnyType> itr = findPrevious(x);

        if (itr.current.next != null) {
            itr.current.next = itr.current.next.next;
        }
    }

    public static <AnyType> void printList(LinkedList<?> list) {
        if (list.isEmpty()) {
            System.out.println("lista eshte bosh");
        } else {
            LinkedListIterator<?> itr = list.first();
            for (; itr.isValid(); itr.advance()) {
                System.out.print(itr.retrieve() + " ");
            }
        }
        System.out.println();
    }


    /*Ushtrimi 1*/
    public double Shuma(LinkedList<Double> list) throws Exception {
        if (list.isEmpty()) throw new Exception("Lista eshte boshe");

        /*LinkedListIterator<Double>itr = list.first();
        double shuma = 0;
        while (itr.isValid()){
            shuma += itr.retrieve();
            itr.advance();
        }
        */
        ListNode<Double> current = list.header.next;
        double shuma = 0;

        while (current != null) {
            shuma += current.element;
            current = current.next;
        }
        return shuma;
    }


    /*Ushtrimi 2*/
    public int size() {
        if (isEmpty()) return 0;

        LinkedListIterator<AnyType> itr = first();
        int size = 0;
        while (itr.isValid()) {
            size++;
            itr.advance();
        }
        /*
        ListNode<AnyType> current = header;
        int size = 0;
        while (current.next!=null){
            size++;
            current = current.next;
        }
         */
        return size;
    }

    /*Ushtrimi 3*/
    public AnyType Value(int k) throws Exception {
        if (isEmpty()) throw new Exception("Lista eshte boshe");

        if (size() < k) {
            throw new Exception("Lista nuk e permban nyjen e:" + k + "-te");
        }

        LinkedListIterator<AnyType> itr = zeroth();

        while (itr.isValid() && k > 0) {
            k--;
            itr.advance();
        }
        return itr.retrieve();
    }

    public AnyType gjejVleren(int k) {
        int i = 0;
        LinkedListIterator<AnyType> itr = this.first();
        while (itr.isValid() && i != k) {
            itr.advance();
            i++;
        }
        if (itr.isValid()) {
            return itr.retrieve();
        } else {
            System.out.println("Indeksi nuk ekziston");
            return null;
        }
    }

    /*Ushtrimi 4*/
    public void Remove_K_Element(AnyType K) throws Exception {
        if (isEmpty()) throw new Exception("Lista eshte boshe");

        ListNode<AnyType> current = header.next;
        ListNode<AnyType> previous = header;

        while (current != null && !current.element.equals(K)) {
            previous = current;
            current = current.next;
        }
        if (current != null) {
            previous.next = current.next;
        } else throw new Exception("Nuk ndodhet ne liste");
    }

    /*Ushtrimi 5*/
    public void Shto_Element(AnyType K, int j) throws Exception {

        if (size() < j - 1) throw new Exception("Nuk mund te shtohet ne liste");

        if (j == 0) throw new Exception("Nuk mund te jete koka e listes");


        LinkedListIterator<AnyType> itr = zeroth();

        while (itr.isValid() && j > 1) {
            j--;
            itr.advance();
        }
        itr.current.next = new ListNode<AnyType>(K, itr.current.next);
    }

    /*Ushtrimi 6*/
    public int ElementetMidis(LinkedListIterator<AnyType> A, LinkedListIterator<AnyType> B) throws Exception {
        if (!(A.isValid() && B.isValid())) {
            throw new Exception("Njeri prej pozicioneve eshte null");
        }
        LinkedListIterator<AnyType> itr = new LinkedListIterator<AnyType>(A.current.next);
        int i = 0;
        while (itr.isValid() && !itr.retrieve().equals(B.retrieve())) {
            i++;
            itr.advance();
        }
        return i;
    }

    /*Ushtrimi 7*/
    public void Max_Min(LinkedList<Double> list) throws Exception {
        if (list.isEmpty()) throw new Exception("Lista eshte boshe");
        if (list.size() == 1) throw new Exception("Nuk mund te gjendet nje Max ose Min");

        ListNode<Double> current = list.header.next;
        ListNode<Double> maxPrev = null;
        ListNode<Double> maxNode = current;

        while (current.next != null) {
            if (current.next.element > maxNode.element) {
                maxPrev = current;
                maxNode = current.next;
            }
            current = current.next;
        }
        if (maxPrev != null) {
            maxPrev.next = maxNode.next;
            maxNode.next = list.header.next;
            list.header.next = maxNode;
        }

        /*
         * Kushti maxPrev != null perdoret per te kontrolluar nese Maksimumi ndodhet menjehere ne fillim.
         * Ne kete rast maxPrev nuk ndryshon vleren null dhe nuk kemi pse te bejme zhvendosje.
         */


        current = list.header.next;
        ListNode<Double> minPrev = list.header;
        ListNode<Double> minNode = current;

        while (current.next != null) {
            if (current.next.element < minNode.element) {
                minPrev = current;
                minNode = current.next;
            }
            current = current.next;
        }

        if (minNode.next != null) {
            minPrev.next = minNode.next;
            current.next = minNode;
            minNode.next = null;
        }

        /*
         * Ne list vendosim Maksimumin te parin pastaj fillojme te gjejme minimumin
         * Ne kete rast minPrev do te ndryshoje te pakten nje here sepse minNode fillon nga nyja e pare
         * qe eshte Maksimumi.Pra ne fund kontrollohet nese jemi ne fund nyjen e fundit te listes ,nese
         * jo do te behet zhvendosja e Miniumit ne fund
         */
    }

    /*Ushtrimi 8*/
    /*
     * Metoda funksionon per te gjitha rastet edhe kur listat jane te renditura.
     */
    public void FshiElemente2Lista(LinkedList<AnyType> L, LinkedList<AnyType> P) throws Exception {
        if (L.isEmpty() || P.isEmpty()) throw new Exception("Njera prej listave eshte bosh");

        LinkedListIterator<?> itr = P.first();

        while (itr.isValid()) {

            while (!L.isEmpty()) {
                try {
                    L.Remove_K_Element((AnyType) itr.retrieve());
                } catch (Exception e) {
                    /*Cdo element qe ndodhet ne Listen P por nuk ndodhet ne Listen L gjeneron nje perjashtim
                      i cili thjesht kapet nga try/catch thjesht per te bere break te loop.
                    */
                    break;
                }
            }
            itr.advance();
        }
    }

    /*Ushtrimi 9*/
    public void inverto() throws Exception {
        if (isEmpty()) throw new Exception("Lista eshte boshe");

        ListNode<AnyType> current = header.next;
        ListNode<AnyType> prev = null, next;

        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        header.next = prev;

    }

    /*Ushtrimi 10*/
    public boolean palindrome() throws Exception {
        ListNode<AnyType> current = header.next;
        int size = size() / 2;


        for (int i = 0; i < size; i++) {
            if (!current.element.equals(Value(size() - i))) {
                return false;
            }
            current = current.next;
        }
        return true;
    }

    /*Ushtrimi 11*/
    public void ShtoPaPrishurRenditjen(AnyType K) throws Exception {
        if (isEmpty()) {
            throw new Exception("Lista eshte boshe");
        }

        ListNode<AnyType> current = header.next;
        ListNode<AnyType> previous = header;
        while (current.next != null && current.element.compareTo(K) < 0) {
            previous = current;
            current = current.next;
        }
        if (current.next != null) previous.next = new ListNode<AnyType>(K, previous.next);
        else current.next = new ListNode<AnyType>(K, null);
    }

    /*Ushtrimi 12*/
    public void NdrroVendin() {
        ListNode<AnyType> current = header.next;
        int i = 1;

        while (current.next != null) {
            if (i % 2 == 1) {
                AnyType temp = current.element;
                current.element = current.next.element;
                current.next.element = temp;
            }
            current = current.next;
            i++;
        }
    }

    public void NdrroVendinFqinj(LinkedListIterator<AnyType> P, String S) throws Exception {
        if (size() < 2) throw new Exception("Nuk mund te ndrrohen vendet");
        if (!P.isValid()) throw new Exception("Referenca eshte null");

        LinkedListIterator<AnyType> itr;
        if (S.equals("Majtas")) {
            itr = findPrevious(P.retrieve());
            if (itr != null) {
                AnyType temp = itr.retrieve();
                itr.current.element = itr.current.next.element;
                itr.current.next.element = temp;
            } else {
                throw new Exception("Nuk mund te shtohet majtas");
            }
        } else if (S.equals("Djathtas")) {
            itr = P;
            if (itr.current.next != null) {
                AnyType temp = itr.retrieve();
                itr.current.element = itr.current.next.element;
                itr.current.next.element = temp;
            } else {
                throw new Exception("Nuk mund te shtohet djathtas");
            }
        } else {
            throw new IllegalArgumentException("Vendos 'Majtas' ose 'Djathtas'");
        }
    }

    /*
     * Kemi dy metoda per nderrimin e vendeve.Njera metode nderron vendet e te gjithe elementeve
     * qe jane fqinje me njeri-tjetrin ne listen fillestare duke filluar nga e majta ne te djathte
     * dhe nese madhesia e listes eshte teke elementi i fundit nuk nderrohet.Ne metoden e dyte e
     * percaktojme vete se cilin element do te nderrojme dhe me cilin fqinj.
     *
     * */

    /*Ushtrimi 13*/
    public void Rrethore() throws Exception {
        if (isEmpty()) throw new Exception("Lista eshte bosh,nuk mund te kthehet rrethore");

        ListNode<AnyType> current = header.next;

        while (current.next != null) {
            current = current.next;
        }
        current.next = header;
    }

    public void BashkoDyLista(LinkedList<AnyType> list) {
        if (isEmpty() || list.isEmpty()) {
            System.out.println("Njera liste eshte boshe");
            return;
        }
        ListNode<AnyType> current = header;

        while (current.next != null) {
            current = current.next;
        }
        ListNode<AnyType> join = list.header;
        current.next = join.next;
        printList(this);
    }

    public LinkedList<AnyType> PaPrishRenditjen(LinkedList<AnyType> L2) {

        if (isEmpty()) {
            System.out.println("L1 eshte boshe");
            return L2;
        }

        if (L2.isEmpty()) {
            System.out.println("L2 eshte boshe");
            return this;
        }

        LinkedList<AnyType> L3 = new LinkedList<AnyType>();

        ListNode<AnyType> current1 = header.next;
        ListNode<AnyType> current2 = L2.header.next;

        while (current1 != null || current2 != null) {
            if (current1 != null && current2 != null) {
                if (current1.element.compareTo(current2.element) < 0) {
                    L3.insertAtEnd(current1);
                    current1 = current1.next;
                } else {
                    L3.insertAtEnd(current2);
                    current2 = current2.next;
                }
            } else if (current1 != null) {
                L3.insertAtEnd(current1);
                current1 = current1.next;
            } else {
                L3.insertAtEnd(current2);
                current2 = current2.next;
            }
        }
        return L3;
    }

    public LinkedList<AnyType> Alternim(LinkedList<AnyType> L2) {
        LinkedList<AnyType> L3 = new LinkedList<AnyType>();
        ListNode<AnyType> current_L1 = this.header.next;
        ListNode<AnyType> current_L2 = L2.header.next;
        int i = 0;
        while (current_L1 != null || current_L2 != null) {
            if (current_L1 != null && current_L2 != null) {
                if (i % 2 == 0) {
                    L3.insertAtEnd(current_L1);
                    current_L1 = current_L1.next;
                } else {
                    L3.insertAtEnd(current_L2);
                    current_L2 = current_L2.next;
                }
                i++;
            } else if (current_L2 != null) {
                L3.insertAtEnd(current_L2);
                current_L2 = current_L2.next;
            } else {
                L3.insertAtEnd(current_L1);
                current_L1 = current_L1.next;
            }
        }
        return L3;
    }

    public void insertAtEnd(ListNode<AnyType> node) {

        ListNode<AnyType> current = header;
        while (current.next != null) {
            current = current.next;
        }
        current.next = new ListNode<>(node.element);
    }


    public void TekACift() throws Exception {

        ListNode<AnyType> current = header.next;
        LinkedList<AnyType> L1 = new LinkedList<AnyType>();
        LinkedList<AnyType> L2 = new LinkedList<AnyType>();

        while (current != null) {

            if ((Double) current.element % 2 == 0 && L1.isEmpty()) {
                L1.insertAtEnd(current);
            } else if ((Double) current.element % 2 == 1 && L2.isEmpty()) {
                L2.insertAtEnd(current);
            } else if ((Double) current.element % 2 == 0) {

                ListNode<AnyType> node = L1.header.next;

                while (node != null) {
                    if (current.element.compareTo(node.element) < 0) {
                        L1.ShtoPaPrishurRenditjen(current.element);
                        return;
                    }
                    node = node.next;
                }
                L1.insertAtEnd(current);
            } else {
                ListNode<AnyType> node = L2.header.next;

                while (node != null) {
                    if (current.element.compareTo(node.element) < 0) {
                        L2.ShtoPaPrishurRenditjen(current.element);
                        return;
                    }
                    node = node.next;
                }
                L2.insertAtEnd(current);
            }
            current = current.next;
        }
        LinkedList.printList(L1);
        LinkedList.printList(L2);


    }

    public void Tek_A_Cift() throws Exception {

        ListNode<AnyType> current = header.next;


        LinkedList<AnyType> L1 = new LinkedList<AnyType>();
        LinkedList<AnyType> L2 = new LinkedList<AnyType>();

        while (current != null) {

            if ((Double) current.element % 2 == 0) L1.insertAtEnd(current);
            else L2.insertAtEnd(current);
            current = current.next;
        }


        LinkedList.printList(L1);
        LinkedList.printList(L2);
    }

    public static void main(String[] args) throws Exception {
        LinkedList<Double> list = new LinkedList<Double>();
        LinkedList<Double> list1 = new LinkedList<Double>();

        LinkedListIterator<Double> itr = list.zeroth();
        LinkedListIterator<Double> itr1 = list1.zeroth();
        for (double i = 0; i < 13; i++) {
            list1.insert(i, itr1);
            itr1.advance();
        }
        for (double i = 7; i < 20; i++) {
            list.insert(i, itr);
            itr.advance();
        }

        System.out.println("Gjatesia e listes se pare: " + list.size());
        System.out.println("Gjatesia e listes se dyte: " + list1.size());

        System.out.println("Lista e pare:");
        printList(list);
        System.out.println("Shuma e elementeve " + list.Shuma(list));

        System.out.println("Vlera e elementit me indeks 4: " + list.Value(4));

        System.out.println("Fshijme elementin me vlere 15.0");
        list.Remove_K_Element(15.0);

        System.out.println("Lista e pare pas fshirjes:");
        printList(list);

        System.out.println("Shtojme element ne indeksin 6");
        list.Shto_Element(3.0, 6);
        printList(list);

        System.out.println("Nr i elementeve qe ndodhen midis 7.0 dhe 13.0: " + list.ElementetMidis(list.find(7.0), list.find(13.0)));

        list.Max_Min(list);
        System.out.println("Lista me Max ne fillim dhe Min ne fund");
        printList(list);

        list.inverto();
        System.out.println("Lista e invertuar:");
        printList(list);

        System.out.println("A eshte palindrome? " + (list.palindrome() ? "Po" : "Jo"));

        System.out.println("Shtojme 17.3 pa prishur renditjen");
        //list.ShtoPaPrishurRenditjen(list,17.3);
        printList(list);

        list.NdrroVendin();
        System.out.println("Lista me vendet e ndrruara");
        printList(list);

        list.FshiElemente2Lista(list, list1);
        System.out.println("Fshijme elementet nga lista e pare qe ndodhen ne listen e dyte:");
        printList(list);

        System.out.println("Ndrrojme nje element");
        list.NdrroVendinFqinj(list.find(18.0), "Djathtas");
        list.NdrroVendinFqinj(list.find(13.0), "Majtas");
        printList(list);


        System.out.println();
        System.out.println();


        System.out.println("Lista e dyte:");
        printList(list1);
        System.out.println("Shuma e elementeve " + list1.Shuma(list1));
        System.out.println("Vlera e elementit me indeks 4: " + list1.Value(4));

        System.out.println("Fshijme elementin me vlere 6.0");
        list1.Remove_K_Element(6.0);

        System.out.println("Lista e dyte pas fshirjes:");
        printList(list1);

        System.out.println("Shtojme element ne indeksin 6");
        list1.Shto_Element(15.0, 6);
        printList(list1);

        System.out.println("Nr i elementeve qe ndodhen midis 3.0 dhe 10.0: " + list1.ElementetMidis(list1.find(3.0), list1.find(10.0)));

        list1.Max_Min(list1);
        System.out.println("Lista me Max ne fillim dhe Min ne fund");
        printList(list1);

        list1.inverto();
        System.out.println("Lista e invertuar:");
        printList(list1);

        System.out.println("A eshte palindrome? " + (list1.palindrome() ? "Po" : "Jo"));

        System.out.println("Shtojme 4.3 pa prishur renditjen");
        //list1.ShtoPaPrishurRenditjen(list1,4.3);
        printList(list1);

        list1.NdrroVendin();
        System.out.println("Lista me vendet e ndrruara");
        printList(list1);

        list1.FshiElemente2Lista(list1, list);
        System.out.println("Fshijme elementet nga lista e dyte qe ndodhen ne listen e pare:");
        printList(list1);

        System.out.println("Ndrrojme nje element");
        list1.NdrroVendinFqinj(list1.find(8.0), "Djathtas");
        list1.NdrroVendinFqinj(list1.find(11.0), "Majtas");
        printList(list1);


        System.out.println();
        System.out.println();


        LinkedList<Double> list2 = new LinkedList<Double>();
        LinkedListIterator<Double> itr2 = list2.zeroth();

        list2.Shto_Element(0.0, 1);
        list2.Shto_Element(1.0, 2);
        list2.Shto_Element(0.0, 3);

        System.out.println("Lista e trete:");
        printList(list2);

        System.out.println("A eshte palindrome? " + (list2.palindrome() ? "Po" : "Jo"));

        list2.makeEmpty();
        for (double i = 0; i < 13; i++) {
            list2.insert(i, itr2);
            itr2.advance();
        }

        System.out.println("Lista e trete:");
        printList(list2);

        System.out.println("Shtojme 12.1 pa prishur renditjen");
        //list2.ShtoPaPrishurRenditjen(list2,12.1);
        printList(list2);

        System.out.println("Shtojme 0.3 pa prishur renditjen");
        //list2.ShtoPaPrishurRenditjen(list2,0.3);
        printList(list2);

        LinkedList<Double> list3 = new LinkedList<Double>();
        LinkedListIterator<Double> itr3 = list3.zeroth();
        for (double i = 0; i < 10; i += 2) {
            list3.insert(i, itr3);
            itr3.advance();
        }

        LinkedList<Double> list4 = new LinkedList<Double>();
        LinkedListIterator<Double> itr4 = list4.zeroth();
        for (double i = -2.0; i < 10; i++) {
            list4.insert(i, itr4);
            itr4.advance();
        }
        //list3.BashkoDyLista(list4);
        //printList(list4);
        LinkedList<Double> list5 = list3.PaPrishRenditjen(list4);
        printList(list5);

        list5.TekACift();


    }
}