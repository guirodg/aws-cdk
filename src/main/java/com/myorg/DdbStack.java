package com.myorg;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.RemovalPolicy;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;

public class DdbStack extends Stack {
    private final Table productEventsDbd;
    public DdbStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public DdbStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        productEventsDbd = Table.Builder.create(this, "ProductEventDb")
                .tableName("product-events")
                .billingMode(BillingMode.PROVISIONED) // Mode de PROVISIONED quantidade de leitura e escrita, por uso
                .readCapacity(1)
                .writeCapacity(1)
                .partitionKey(Attribute.builder() // Define nosso atributo PK
                        .name("pk")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder() // Define nosso atributo SK sort key
                        .name("sk")
                        .type(AttributeType.STRING)
                        .build())
                .timeToLiveAttribute("ttl")
                .removalPolicy(RemovalPolicy.DESTROY) // Caso apagar a stack destruir a tabela
                .build();
    }

    public Table getProductEventsDbd() {
        return productEventsDbd;
    }
}
