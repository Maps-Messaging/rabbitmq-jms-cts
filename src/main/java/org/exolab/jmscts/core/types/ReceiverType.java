/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id$
 */

package org.exolab.jmscts.core.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.util.Hashtable;

/**
 * Class ReceiverType.
 *
 * @version $Revision$ $Date$
 */
public class ReceiverType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The synchronous type
     */
    public static final int SYNCHRONOUS_TYPE = 0;

    /**
     * The instance of the synchronous type
     */
    public static final ReceiverType SYNCHRONOUS = new ReceiverType(SYNCHRONOUS_TYPE, "synchronous");

    /**
     * The asynchronous type
     */
    public static final int ASYNCHRONOUS_TYPE = 1;

    /**
     * The instance of the asynchronous type
     */
    public static final ReceiverType ASYNCHRONOUS = new ReceiverType(ASYNCHRONOUS_TYPE, "asynchronous");

    /**
     * The durable_synchronous type
     */
    public static final int DURABLE_SYNCHRONOUS_TYPE = 2;

    /**
     * The instance of the durable_synchronous type
     */
    public static final ReceiverType DURABLE_SYNCHRONOUS = new ReceiverType(DURABLE_SYNCHRONOUS_TYPE, "durable_synchronous");

    /**
     * The durable_asynchronous type
     */
    public static final int DURABLE_ASYNCHRONOUS_TYPE = 3;

    /**
     * The instance of the durable_asynchronous type
     */
    public static final ReceiverType DURABLE_ASYNCHRONOUS = new ReceiverType(DURABLE_ASYNCHRONOUS_TYPE, "durable_asynchronous");

    /**
     * The browser type
     */
    public static final int BROWSER_TYPE = 4;

    /**
     * The instance of the browser type
     */
    public static final ReceiverType BROWSER = new ReceiverType(BROWSER_TYPE, "browser");

    /**
     * The shared_durable_asynchronous type
     */
    public static final int SHARED_DURABLE_ASYNCHRONOUS_TYPE = 5;

    /**
     * The instance of the durable_asynchronous type
     */
    public static final ReceiverType SHARED_DURABLE_ASYNCHRONOUS = new ReceiverType(SHARED_DURABLE_ASYNCHRONOUS_TYPE, "shared_durable_asynchronous");


    /**
     * The shared_durable_asynchronous type
     */
    public static final int SHARED_DURABLE_SYNCHRONOUS_TYPE = 6;

    /**
     * The instance of the durable_asynchronous type
     */
    public static final ReceiverType SHARED_DURABLE_SYNCHRONOUS = new ReceiverType(SHARED_DURABLE_SYNCHRONOUS_TYPE, "shared_durable_synchronous");


    /**
     * Field _memberTable
     */
    private static java.util.Hashtable<String, ReceiverType> _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private ReceiverType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.exolab.jmscts.core.types.ReceiverType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ReceiverType
     */
    public static java.util.Enumeration<ReceiverType> enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate()

    /**
     * Method getTypeReturns the type of this ReceiverType
     */
    public int getType()
    {
        return this.type;
    } //-- int getType()

    /**
     * Method init
     */
    private static java.util.Hashtable<String, ReceiverType> init()
    {
        Hashtable<String, ReceiverType> members = new Hashtable<String, ReceiverType>();
        members.put("synchronous", SYNCHRONOUS);
        members.put("asynchronous", ASYNCHRONOUS);
        members.put("durable_synchronous", DURABLE_SYNCHRONOUS);
        members.put("durable_asynchronous", DURABLE_ASYNCHRONOUS);
        members.put("browser", BROWSER);
        members.put("shared_durable_synchronous", SHARED_DURABLE_SYNCHRONOUS);
        members.put("shared_durable_asynchronous", SHARED_DURABLE_ASYNCHRONOUS);
        return members;
    } //-- java.util.Hashtable init()

    /**
     * Method toStringReturns the String representation of this
     * ReceiverType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString()

    /**
     * Method valueOfReturns a new ReceiverType based on the given
     * String value.
     *
     * @param string
     */
    public static org.exolab.jmscts.core.types.ReceiverType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ReceiverType";
            throw new IllegalArgumentException(err);
        }
        return (ReceiverType) obj;
    } //-- org.exolab.jmscts.core.types.ReceiverType valueOf(java.lang.String)

}
