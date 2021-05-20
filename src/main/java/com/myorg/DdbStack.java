package com.myorg;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.dynamodb.*;

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

        // Auto Scale DB Leitura
        productEventsDbd.autoScaleReadCapacity(EnableScalingProps.builder()
                .minCapacity(1)
                .maxCapacity(4)
                .build())
                .scaleOnUtilization(UtilizationScalingProps.builder()
                        .targetUtilizationPercent(50)
                        .scaleInCooldown(Duration.seconds(30))
                        .scaleOutCooldown(Duration.seconds(30))
                        .build());
        // Auto Scale DB Escrita
        productEventsDbd.autoScaleWriteCapacity(EnableScalingProps.builder()
                .minCapacity(1)
                .maxCapacity(4)
                .build())
                .scaleOnUtilization(UtilizationScalingProps.builder()
                        .targetUtilizationPercent(50)
                        .scaleInCooldown(Duration.seconds(30))
                        .scaleOutCooldown(Duration.seconds(30))
                        .build());
    }

    public Table getProductEventsDbd() {
        return productEventsDbd;
    }
}
