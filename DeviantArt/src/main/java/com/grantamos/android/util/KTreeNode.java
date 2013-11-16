package com.grantamos.android.util;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Grant on 11/12/13.
 */
public class KTreeNode<T> implements Serializable {
    T object;
    KTreeNode parent;
    int depth = 0;
    public ArrayList<KTreeNode> children;
    boolean isMarked = false;

    public KTreeNode(T t, KTreeNode parent) {
        this.object = t;
        this.parent = parent;

        children = new ArrayList<KTreeNode>();

        KTreeNode<T> node = this.parent;
        while(node != null){
            depth++;
            node = node.parent;
        }
    }

    public T getValue() {
        return object;
    }

    public int getDepth() {return depth;}

    public KTreeNode<T> getParent() { return parent; }

    public void addChild(KTreeNode child){
        children.add(child);
    }

    public KTreeNode<T> getChild(int i){
        if(children.size() == 0)
            return null;

        KTreeNode<T> item = children.get(0);

        if(i > 0){
            item = children.get(i);
        }

        while(i < 0){
            item = item.getParent();
            i++;
        }

        return item;
    }

    public int childCount(){
        return children.size();
    }

    public int countTreeSize() {
        int count = 1;

        for(int i = 0; i < children.size(); i++)
            count += children.get(i).countTreeSize();

        return count - 1;
    }

    public void setIsMarked(boolean isMarked) { this.isMarked = isMarked; }

    public boolean getIsMarked() { return isMarked; }

    public KTreeNode<T> inOrderTraverse(int position) {

        if(position < 0 || children.size() == 0)
            return null;

        for(int i = 0; i < children.size(); i++){
            KTreeNode consider = children.get(i);
            if(consider.getIsMarked())
                continue;

            position--;
        }

        return null;
    }

    public boolean hasParent() { return parent != null; }

    @Override
    public String toString() { return object.toString(); }
}